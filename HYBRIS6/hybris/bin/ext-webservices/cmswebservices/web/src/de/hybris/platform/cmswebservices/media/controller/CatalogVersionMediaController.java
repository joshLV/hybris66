/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cmswebservices.media.controller;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.header.LocationHeaderResource;
import de.hybris.platform.cmsfacades.media.MediaFacade;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;


/*
 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exceptions) : It is
 * perfectly acceptable not to handle "e" here
 */
@SuppressWarnings("squid:S1166")
/**
 * Controller that provides media.
 *
 * @pathparam catalogId The unique code of the Catalog item.
 * @pathparam versionId The version of the catalog
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}" + CatalogVersionMediaController.MEDIA_URI_PATH)
public class CatalogVersionMediaController
{
	private static final String DESCRIPTION = "description";
	private static final String ALT_TEXT = "altText";
	private static final String CODE = "code";
	public static final String MEDIA_URI_PATH = "/media";
	private static final String UTF_8 = "UTF-8";

	@Resource
	private MediaFacade mediaFacade;

	@Resource
	private LocationHeaderResource locationHeaderResource;

	@Resource
	private DataMapper dataMapper;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Upload multipart media", notes = "Upload a media.")
	@ApiResponses({
			@ApiResponse(code = 400, message = "When an error occurs parsing the MultipartFile (IOException) or when the media query parameters provided contain validation errors (WebserviceValidationException)"),
			@ApiResponse(code = 200, message = "The newly created Media item", response = MediaData.class)
	})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "The code to use for the newly created media.", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "altText", value = "The alternative text to use for the newly created media.", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "description", value = "The description to use for the newly created media.", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "multiPart", value = "The file that was uploaded for the new media.", required = false, dataType = "string", paramType = "query")
	})
	public MediaData uploadMultipartMedia(
			@ApiParam(value = "The unique identifier of the catalog for which to link the new media.", required = true) @PathVariable("catalogId") final String catalogId,
			@ApiParam(value = "The specific catalog version to which the new media will be associated to.", required = true) @PathVariable("versionId") final String versionId,
			@ApiParam(value = "The MediaData containing the data for the associated media item to be created.", required = true) @ModelAttribute("media") final MediaData media,
			@ApiParam(value = "The file representing the actual binary contents of the media to be created.", required = true) @RequestParam("file") final MultipartFile multiPart,
			final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, WebserviceValidationException
	{
		media.setCatalogId(catalogId);
		media.setCatalogVersion(versionId);

		try
		{
			final de.hybris.platform.cmsfacades.data.MediaData convertedMediaData = //
					getDataMapper().map(media, de.hybris.platform.cmsfacades.data.MediaData.class);
			final de.hybris.platform.cmsfacades.data.MediaData newMedia = //
					getMediaFacade().addMedia(convertedMediaData, getFile(multiPart, multiPart.getInputStream()));

			response.addHeader(CmswebservicesConstants.HEADER_LOCATION,
					getLocationHeaderResource().createLocationForChildResource(request, newMedia.getCode()));
			return getDataMapper().map(newMedia, MediaData.class);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	@RequestMapping(value = "/**", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
			value = "Get media by code",
			notes = "Get a media by code. The resource will accept any GET ../media/**, which "
			+ "includes paths and image extensions at the end. To allow this resource to accept any image extension like .jpg, .gif, .png, "
			+ "Spring MVC needs to be configured accordingly. See spring mvc configuration\n"
			+ "<mvc:annotation-driven content-negotiation-manager=\"contentNegotiationManager\"> where\n"
			+ "org.springframework.web.accept.ContentNegotiationManagerFactoryBean.favorPathExtension = false")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "code", value = "The unique code of the Media item.", required = true, dataType = "string", paramType = "query"),
	})
	@ApiResponses({
			@ApiResponse(code = 400, message = "When the media code requested cannot be found."),
			@ApiResponse(code = 200, message = "The Media item matching the code", response = MediaData.class)
	})
	public MediaData getMediaByCode(
			@ApiParam(value = "The HttpServletRequest containing the unique code of the Media item", required = true) final HttpServletRequest request)
			throws MediaNotFoundException
	{
		final Optional<String> optionalMediaCode = parseMediaCode(request);

		final de.hybris.platform.cmsfacades.data.MediaData media = getMediaFacade() //
				.getMediaByCode(optionalMediaCode.orElseThrow(() -> new MediaNotFoundException("Media code cannot be empty")));

		return getDataMapper().map(media, MediaData.class);
	}

	/**
	 * Parses the Request URI after the Media code, which is defined by everything after the /media less the first '/', if
	 * present.
	 * @param request the http servlet request
	 * @return an optional object to hold the parsed media code
	 */
	protected Optional<String> parseMediaCode(final HttpServletRequest request)
	{
		// we know that the URI will always have the MEDIA_URI_PATH at the end
		final String uri = StringUtils.substringAfter(request.getRequestURI(), MEDIA_URI_PATH);
		if (uri.trim().length() > 0)
		{
			// returns the remaining path without the first '/'
			try
			{
				return Optional.of(java.net.URLDecoder.decode(uri.substring(1), UTF_8));
			}
			catch (final UnsupportedEncodingException e)
			{
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	/**
	 * Create a new media DTO from request parameters.
	 *
	 * @param request
	 *           - the http servlet request
	 * @return a media DTO
	 * @deprecated since 6.4, method no longer used
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.4")
	public MediaData getMedia(final HttpServletRequest request)
	{
		final MediaData media = new MediaData();
		media.setCode(request.getParameter(CODE));
		media.setAltText(request.getParameter(ALT_TEXT));
		media.setDescription(request.getParameter(DESCRIPTION));
		return media;
	}

	/**
	 * Create a new media file DTO from the {@code MultipartFile}.
	 *
	 * @param file
	 *           - a Spring {@code MultipartFile}
	 * @param inputStream
	 *           - an input stream used to read the file
	 * @return a media file DTO
	 */
	public MediaFileDto getFile(final MultipartFile file, final InputStream inputStream)
	{
		final MediaFileDto mediaFile = new MediaFileDto();
		mediaFile.setInputStream(inputStream);
		mediaFile.setName(file.getOriginalFilename());
		mediaFile.setSize(file.getSize());
		mediaFile.setMime(file.getContentType());
		return mediaFile;
	}

	protected MediaFacade getMediaFacade()
	{
		return mediaFacade;
	}

	public void setMediaFacade(final MediaFacade mediaFacade)
	{
		this.mediaFacade = mediaFacade;
	}

	protected LocationHeaderResource getLocationHeaderResource()
	{
		return locationHeaderResource;
	}

	public void setLocationHeaderResource(final LocationHeaderResource locationHeaderResource)
	{
		this.locationHeaderResource = locationHeaderResource;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}
}