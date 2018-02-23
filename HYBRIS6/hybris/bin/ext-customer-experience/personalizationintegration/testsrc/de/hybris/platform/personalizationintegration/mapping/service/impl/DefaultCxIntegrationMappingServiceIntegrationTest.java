/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.personalizationintegration.mapping.service.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.service.impl.DefaultCxIntegrationMappingService;
import de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultCxIntegrationMappingServiceIntegrationTest extends ServicelayerTest
{
	private static final String SEGMENT1 = "segment1";
	private static final String SEGMENT2 = "segment2";
	private static final String SEGMENT3 = "segment3";

	@Resource
	private DefaultCxIntegrationMappingService defaultCxIntegrationMappingService;

	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private SessionService sessionService;

	@Before
	public void setupSampleData() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importData(new ClasspathImpExResource("/personalizationintegration/test/testdata_personalizationintegration.impex", "UTF-8"));
	}

	@Test
	public void shouldReturnSegmentsFromMultiplMappers()
	{
		//given
		final List<String> testValues = new ArrayList<>();
		testValues.add("seg1");
		testValues.add("seg2");

		//when
		final Optional<MappingData> profile = defaultCxIntegrationMappingService.mapExternalData(testValues, "testConverterName");

		//then
		Assert.assertTrue(profile.isPresent());
		Assert.assertNotNull(profile.get().getSegments());
		Assert.assertEquals(4, profile.get().getSegments().size());

		final List<String> segmentNames = profile.get().getSegments().stream().map(s -> s.getCode()).collect(Collectors.toList());
		Assert.assertTrue(segmentNames.containsAll(Arrays.asList("segment1", "segment2", "seg1-test", "seg2-test")));
	}

	@Test
	public void shouldAssignUserToSegments()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, false);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2), segmentCodes);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldPutUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2, SEGMENT3), segmentCodes);
	}

	@Test
	public void shouldAddNewUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1));

		final MappingData mappingData = createMappingData(SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT2, SEGMENT3), segmentCodes);
	}

	@Test
	public void shouldNarrowUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1, SEGMENT2, SEGMENT3));

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2), segmentCodes);
	}


	@Test
	public void shouldExtendUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1, SEGMENT2));

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2, SEGMENT3), segmentCodes);
	}

	@Test
	public void shouldMixNewAndExistingUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1, SEGMENT2));

		final MappingData mappingData = createMappingData(SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT2, SEGMENT3), segmentCodes);
	}

	@Test
	public void shouldClearUserToSegmentsInSessionForAnonymous()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1, SEGMENT2));

		final MappingData mappingData = createMappingData();

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		Assert.assertThat(u2ss, CoreMatchers.is(Matchers.empty()));
	}

	@Test
	public void shouldNotChangeUsertoSegmentInSessionForNullMapingData()
	{
		//given
		final UserModel user = userService.getAnonymousUser();

		sessionService.setAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY, createUserToSegments(user, SEGMENT1, SEGMENT2));

		final MappingData mappingData = null;

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		Assert.assertTrue(userToSegments.isEmpty());

		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);

		u2ss.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = u2ss.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2), segmentCodes);
	}

	@Test
	public void shouldAssignUserToCreatedSegments()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2, SEGMENT3), segmentCodes);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldAssignUserToCreatedSegmentsWithDefaultAffinity()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);

		final MappingData mappingData = createMappingData(SEGMENT1, SEGMENT2);
		mappingData.getSegments().add(createSegmentMappingData(SEGMENT3, null));

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.stream()
				.filter(u2s -> StringUtils.equals(SEGMENT1, u2s.getSegment().getCode()) || StringUtils.equals(SEGMENT2, u2s.getSegment().getCode()))
				.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));
		userToSegments.stream()
				.filter(u2s -> StringUtils.equals(SEGMENT3, u2s.getSegment().getCode()))
				.forEach(s -> Assert.assertTrue(new BigDecimal("1").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1, SEGMENT2, SEGMENT3), segmentCodes);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldAssignUserToNewSegmentsAndRemoveOldOne()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);

		final MappingData mappingData = createMappingData(SEGMENT2, SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT2, SEGMENT3), segmentCodes);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldModifyExistingSegmentOnly()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);
		final MappingData mappingData = createMappingData(SEGMENT1);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1), segmentCodes);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldRemoveUser2SegmentRelation()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);
		final MappingData mappingData = createMappingData(SEGMENT3);

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, false);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();
		userToSegments.forEach(s -> Assert.assertTrue(new BigDecimal("0.5").compareTo(s.getAffinity()) == 0));

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(0, segmentCodes.size());

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	@Test
	public void shouldChooseBiggestAffinityForDuplicatesToSegments()
	{
		//given
		final UserModel user = userService.getUserForUID("customer1@hybris.com");
		verifyUserHasSegment1Affinity1(user);

		final MappingData mappingData = createMappingData(SEGMENT3);
		mappingData.getSegments().add(createSegmentMappingData(SEGMENT3, new BigDecimal("0.6")));
		mappingData.getSegments().add(createSegmentMappingData(SEGMENT3, new BigDecimal("0.75")));

		//when
		defaultCxIntegrationMappingService.assignSegmentsToUser(user, mappingData, true);

		//then
		modelService.refresh(user);
		final Collection<CxUserToSegmentModel> userToSegments = user.getUserToSegments();

		final Set<String> segmentCodes = userToSegments.stream().map(CxUserToSegmentModel::getSegment).map(CxSegmentModel::getCode)
				.collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT3), segmentCodes);

		Assert.assertTrue(userToSegments.stream().filter(u2s -> StringUtils.equals(SEGMENT3, u2s.getSegment().getCode())).count() == 1);
		final BigDecimal s3Affinity = userToSegments.stream().filter(u2s -> StringUtils.equals(SEGMENT3, u2s.getSegment().getCode())).findAny().get().getAffinity();
		Assert.assertTrue(new BigDecimal("0.75").compareTo(s3Affinity) == 0);

		verifyThatThereIsNoUserToSegmentsInSession();
	}

	private MappingData createMappingData(final String... segments)
	{
		final MappingData result = new MappingData();
		result.setSegments(new ArrayList<>());

		for (final String segment : segments)
		{
			final SegmentMappingData data = createSegmentMappingData(segment, new BigDecimal("0.5"));
			result.getSegments().add(data);
		}

		return result;
	}

	private SegmentMappingData createSegmentMappingData(final String code, final BigDecimal affinity){
		final SegmentMappingData data = new SegmentMappingData();
		data.setCode(code);
		data.setAffinity(affinity);
		return data;
	}

	private Collection<CxUserToSegmentModel> createUserToSegments(final UserModel user, final String... segments){
		Collection<CxUserToSegmentModel> cxUserToSegmentModels = new ArrayList<>();

		Arrays.asList(segments).stream()
				.map(s -> createAnonymousUserToSegmentForSegment(user, s, new BigDecimal("0.5")))
				.forEach(cxUserToSegmentModels::add);

		return cxUserToSegmentModels;
	}

	private CxUserToSegmentModel createAnonymousUserToSegmentForSegment(final UserModel user, final String segmentCode, final BigDecimal affinity){
		final CxUserToSegmentModel cxUserToSegmentModel = modelService.create(CxUserToSegmentModel.class);
		final CxSegmentModel cxSegmentModel = modelService.create(CxSegmentModel.class);
		cxSegmentModel.setCode(segmentCode);
		cxUserToSegmentModel.setUser(user);
		cxUserToSegmentModel.setSegment(cxSegmentModel);
		cxUserToSegmentModel.setAffinity(affinity);

		return cxUserToSegmentModel;
	}
	/**
	 * Just verify that the sample data wasn't modified which would invalidate some of the tests here.
	 *
	 * @param user
	 */
	protected void verifyUserHasSegment1Affinity1(final UserModel user)
	{
		final Set<String> currentSegmentCodes = user.getUserToSegments().stream().map(CxUserToSegmentModel::getSegment)
				.map(CxSegmentModel::getCode).collect(Collectors.toSet());
		Assert.assertEquals(Sets.newHashSet(SEGMENT1), currentSegmentCodes);
	}

	protected void verifyThatThereIsNoUserToSegmentsInSession(){
		final Collection<CxUserToSegmentModel> u2ss = sessionService.getAttribute(PersonalizationservicesConstants.ANONYMOUS_USER_TO_SEGMENTS_SESSION_KEY);
		Assert.assertTrue(CollectionUtils.isEmpty(u2ss));
	}

}
