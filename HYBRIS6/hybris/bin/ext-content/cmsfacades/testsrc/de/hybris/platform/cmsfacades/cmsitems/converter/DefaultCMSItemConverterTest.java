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
package de.hybris.platform.cmsfacades.cmsitems.converter;

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.cms2.model.contents.CMSItemModel.UID;
import static de.hybris.platform.cms2.model.contents.CMSItemModel._TYPECODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CLONE_COMPONENT;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_COMPONENT_UUID;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.SESSION_CLONE_COMPONENT_CLONE_MODEL;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.SESSION_CLONE_COMPONENT_SOURCE_MAP;
import static de.hybris.platform.core.PK.fromLong;
import static de.hybris.platform.core.model.ItemModel.ITEMTYPE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.cloning.strategy.impl.ComponentCloningStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminItemService;
import de.hybris.platform.cmsfacades.cmsitems.AttributeContentConverter;
import de.hybris.platform.cmsfacades.cmsitems.AttributeContentValidator;
import de.hybris.platform.cmsfacades.cmsitems.AttributeStrategyConverterProvider;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemValidator;
import de.hybris.platform.cmsfacades.cmsitems.CloneComponentContextProvider;
import de.hybris.platform.cmsfacades.cmsitems.OriginalClonedItemProvider;
import de.hybris.platform.cmsfacades.common.function.Converter;
import de.hybris.platform.cmsfacades.common.populator.impl.DefaultLocalizedPopulator;
import de.hybris.platform.cmsfacades.common.predicate.attributes.NestedOrPartOfAttributePredicate;
import de.hybris.platform.cmsfacades.common.validator.ValidatableService;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.languages.LanguageFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.util.JSONMatcher;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSItemConverterTest
{

	private static final String EN = "en";
	private static final String FR = "fr";

	private static final String COLLECTION_TYPE = "CollectionType";

	private static final String QUALIFIER_PART_0 = "qualifierPart0";
	private static final String QUALIFIER_0 = "qualifier0";
	private static final String QUALIFIER_1 = "qualifier1";
	private static final String QUALIFIER_2 = "qualifier2";
	private static final String QUALIFIER_3 = "qualifier3";
	private static final String QUALIFIER_4 = "qualifier4";
	private static final String QUALIFIER_5 = "qualifier5";
	private static final String QUALIFIER_6 = "qualifier6";
	private static final String QUALIFIER_7 = "qualifier7";
	private static final String QUALIFIER_8 = "qualifier8";
	private static final String QUALIFIER_9 = "qualifier9";
	private static final String QUALIFIER_10 = "qualifier10";

	private static String SUB_TYPE_TYPE = "subType";
	private static String CMS_SUB_TYPE_TYPE = "cmsSubType";

	public static class MainClass extends ItemModel
	{

		@Override
		public String getItemtype()
		{
			return CMSItemModel._TYPECODE;
		}
	}

	public static class SubClass extends ItemModel
	{

		@Override
		public String getItemtype()
		{
			return SUB_TYPE_TYPE;
		}
	}


	public static class CMSItemSubClass extends CMSItemModel
	{

		@Override
		public String getItemtype()
		{
			return CMS_SUB_TYPE_TYPE;
		}
	}


	//----------------------------------------------------
	// DefaultLocalizedPopulator would be difficult to mock because for the populateAsMap signature, will then use a real on here but with  its own dependencies mocked
	@Mock
	private LanguageFacade languageFacade;
	@Mock
	private CommonI18NService commonI18NService;
	@InjectMocks
	private DefaultLocalizedPopulator localizedPopulator;
	//----------------------------------------------------

	@Mock
	private CMSAdminItemService cmsAdminItemService;

	@Mock
	private TypeService typeService;

	@Mock
	private ModelService modelService;

	@Mock
	private AttributeStrategyConverterProvider attributeStrategyConverter;

	@Mock
	private Converter<Date, String> dateConverter;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private AttributeContentValidator baseAttributeContentValidator;
	@Mock
	private AttributeContentValidator extendedAttributeContentValidator;

	@Mock
	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;

	@Spy
	@InjectMocks
	private DefaultCMSItemConverter cmsItemConverter;

	@Mock
	private ValidatableService validatableService;

	@Mock
	private ValidationErrorsProvider validationErrorsProvider;

	@Mock
	private Predicate<AttributeDescriptorModel> nestedAttributePredicate;

	@InjectMocks
	private NestedOrPartOfAttributePredicate nestedOrPartOfAttributePredicate;

	@Mock
	private MainClass cmsItem;
	@Mock
	private SubClass cmsItemProperty1;
	@Mock
	private CMSItemSubClass cmsItemProperty2;
	@Mock
	private SubClass modifiedCmsItemPartOfProperty;
	@Mock
	private SubClass newCmsItemPartOfProperty1;
	@Mock
	private CMSItemSubClass newCmsItemPartOfProperty2;
	@Mock
	private SubClass modifiedEmbeddedProperty;
	@Mock
	private CMSItemSubClass newEmbeddedProperty;
	@Mock
	private OriginalClonedItemProvider originalClonedItemProvider;
	@Mock
	private ComponentCloningStrategy componentCloningStrategy;
	@Mock
	private CloneComponentContextProvider cloneComponentContextProvider;
	@Mock
	private AttributeStrategyConverterProvider cloneAttributeStrategyConverter;

	private final String uuid1 = "uuid1";
	private final String uuid2 = "uuid2";

	@Mock
	private ComposedTypeModel mainComposedType;
	@Mock
	private ComposedTypeModel subClassComposedType;
	@Mock
	private ComposedTypeModel cmsSubClassComposedType;

	@Mock
	private AttributeDescriptorModel partOfAttributeDescriptor0;
	@Mock
	private AttributeDescriptorModel attributeDescriptor0;
	@Mock
	private AttributeDescriptorModel attributeDescriptor1;
	@Mock
	private AttributeDescriptorModel attributeDescriptor2;
	@Mock
	private AttributeDescriptorModel attributeDescriptor3;
	@Mock
	private AttributeDescriptorModel attributeDescriptor4;
	@Mock
	private AttributeDescriptorModel attributeDescriptor5;
	@Mock
	private AttributeDescriptorModel attributeDescriptor6;
	@Mock
	private AttributeDescriptorModel attributeDescriptor7;
	@Mock
	private AttributeDescriptorModel attributeDescriptor8;
	@Mock
	private AttributeDescriptorModel attributeDescriptor9;
	@Mock
	private AttributeDescriptorModel attributeDescriptor10;


	@Mock
	private AttributeDescriptorModel partOfAttributeDescriptor1;
	@Mock
	private AttributeDescriptorModel partOfAttributeDescriptor2;

	@Mock
	private Map<AttributeDescriptorModel, Object> resultOfConversion;
	@Mock
	private TypeModel collectionTypeModel;
	@Mock
	private TypeModel simpleTypeModel;

	@Mock
	private AttributeContentConverter partOfAttributeContentConverter0;
	@Mock
	private AttributeContentConverter attributeContentConverter0;
	@Mock
	private AttributeContentConverter attributeContentConverter2;
	@Mock
	private AttributeContentConverter attributeContentConverter4;
	@Mock
	private AttributeContentConverter attributeContentConverter6;
	@Mock
	private AttributeContentConverter attributeContentConverter8;
	@Mock
	private AttributeContentConverter attributeContentConverter9;

	@Mock
	private CMSItemValidator<ItemModel> cmsItemValidatorCreate;

	@Mock
	private CMSItemValidator<ItemModel> cmsItemValidatorUpdate;

	@Captor
	private ArgumentCaptor<String> propertyCator;
	@Captor
	private ArgumentCaptor<Object> valueCaptor;

	private ItemData getItemData(final String itemId)
	{
		final ItemData itemData = new ItemData();
		itemData.setItemId(itemId);
		return itemData;
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws IllegalAccessException, InstantiationException
	{
		//0 standard
		//1 partOf
		//2 localized
		//3 localized partOf
		//4 collection
		//5 collection partOf
		//6 localized collection
		//7 localized collection partOf

		final Set<Predicate<AttributeDescriptorModel>> nestedAttributePredicateSet = new HashSet<>();
		nestedAttributePredicateSet.add(nestedAttributePredicate);
		nestedOrPartOfAttributePredicate.setNestedAttributePredicates(nestedAttributePredicateSet);
		cmsItemConverter.setLocalizedPopulator(localizedPopulator);
		cmsItemConverter.setNestedOrPartOfAttributePredicate(nestedOrPartOfAttributePredicate);

		final LanguageData languageEN = new LanguageData();
		languageEN.setIsocode(EN);
		final LanguageData languageFR = new LanguageData();
		languageFR.setIsocode(FR);
		when(languageFacade.getLanguages()).thenReturn(Lists.newArrayList(languageEN, languageFR));
		when(commonI18NService.getLocaleForIsoCode(EN)).thenReturn(ENGLISH);
		when(commonI18NService.getLocaleForIsoCode(FR)).thenReturn(FRENCH);

		when(cmsItem.getItemtype()).thenReturn(_TYPECODE);
		when(cmsItemProperty1.getItemtype()).thenReturn(SUB_TYPE_TYPE);
		when(cmsItemProperty2.getItemtype()).thenReturn(CMS_SUB_TYPE_TYPE);
		when(modifiedCmsItemPartOfProperty.getItemtype()).thenReturn(SUB_TYPE_TYPE);
		when(newCmsItemPartOfProperty1.getItemtype()).thenReturn(SUB_TYPE_TYPE);
		when(newCmsItemPartOfProperty2.getItemtype()).thenReturn(CMS_SUB_TYPE_TYPE);

		//
		when(typeService.getComposedTypeForCode(_TYPECODE)).thenReturn(mainComposedType);
		when(typeService.getComposedTypeForCode(SUB_TYPE_TYPE)).thenReturn(subClassComposedType);
		when(typeService.getComposedTypeForCode(CMS_SUB_TYPE_TYPE)).thenReturn(cmsSubClassComposedType);

		doReturn(MainClass.class).when(typeService).getModelClass(mainComposedType);
		doReturn(SubClass.class).when(typeService).getModelClass(subClassComposedType);
		doReturn(CMSItemSubClass.class).when(typeService).getModelClass(cmsSubClassComposedType);

		//
		final ComposedTypeModel superComposedType = mock(ComposedTypeModel.class);
		when(superComposedType.getDeclaredattributedescriptors()).thenReturn(
				asList(attributeDescriptor4, attributeDescriptor5, attributeDescriptor6, attributeDescriptor7, attributeDescriptor8,
						attributeDescriptor9, attributeDescriptor10));
		when(mainComposedType.getDeclaredattributedescriptors()).thenReturn(
				asList(attributeDescriptor0, attributeDescriptor1, attributeDescriptor2, attributeDescriptor3));
		when(mainComposedType.getCode()).thenReturn(CMSItemModel._TYPECODE);
		when(mainComposedType.getAllSuperTypes()).thenReturn(Arrays.asList(superComposedType));

		when(subClassComposedType.getDeclaredattributedescriptors()).thenReturn(asList(partOfAttributeDescriptor0));
		when(subClassComposedType.getCode()).thenReturn(SUB_TYPE_TYPE);
		when(cmsSubClassComposedType.getDeclaredattributedescriptors()).thenReturn(asList(partOfAttributeDescriptor0));
		when(cmsSubClassComposedType.getCode()).thenReturn(CMS_SUB_TYPE_TYPE);

		when(collectionTypeModel.getItemtype()).thenReturn(COLLECTION_TYPE);
		when(simpleTypeModel.getItemtype()).thenReturn("someType");
		//
		when(partOfAttributeDescriptor0.getQualifier()).thenReturn(QUALIFIER_PART_0);
		when(partOfAttributeDescriptor0.getAttributeType()).thenReturn(simpleTypeModel);
		when(partOfAttributeDescriptor0.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(partOfAttributeDescriptor0)).thenReturn((Class)String.class);

		when(attributeDescriptor0.getQualifier()).thenReturn(QUALIFIER_0);
		when(attributeDescriptor0.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor0.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor0)).thenReturn((Class)attributeDescriptor0.getClass());

		when(attributeDescriptor1.getQualifier()).thenReturn(QUALIFIER_1);
		when(attributeDescriptor1.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor1.getPartOf()).thenReturn(true);
		when(attributeDescriptor1.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor1)).thenReturn((Class)attributeDescriptor1.getClass());

		when(attributeDescriptor2.getQualifier()).thenReturn(QUALIFIER_2);
		when(attributeDescriptor2.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor2.getLocalized()).thenReturn(true);
		when(attributeDescriptor2.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor2)).thenReturn((Class)attributeDescriptor2.getClass());

		when(attributeDescriptor3.getQualifier()).thenReturn(QUALIFIER_3);
		when(attributeDescriptor3.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor3.getPartOf()).thenReturn(true);
		when(attributeDescriptor3.getLocalized()).thenReturn(true);
		when(attributeDescriptor3.getWritable()).thenReturn(true);

		when(attributeDescriptor4.getQualifier()).thenReturn(QUALIFIER_4);
		when(attributeDescriptor4.getAttributeType()).thenReturn(collectionTypeModel);
		when(attributeDescriptor4.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor4)).thenReturn((Class)LinkedList.class);

		when(attributeDescriptor5.getQualifier()).thenReturn(QUALIFIER_5);
		when(attributeDescriptor5.getAttributeType()).thenReturn(collectionTypeModel);
		when(attributeDescriptor5.getPartOf()).thenReturn(true);
		when(attributeDescriptor5.getWritable()).thenReturn(true);

		when(attributeDescriptor6.getQualifier()).thenReturn(QUALIFIER_6);
		when(attributeDescriptor6.getAttributeType()).thenReturn(collectionTypeModel);
		when(attributeDescriptor6.getLocalized()).thenReturn(true);
		when(attributeDescriptor6.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor6)).thenReturn((Class)LinkedList.class);

		when(attributeDescriptor7.getQualifier()).thenReturn(QUALIFIER_7);
		when(attributeDescriptor7.getAttributeType()).thenReturn(collectionTypeModel);
		when(attributeDescriptor7.getPartOf()).thenReturn(true);
		when(attributeDescriptor7.getLocalized()).thenReturn(true);
		when(attributeDescriptor7.getWritable()).thenReturn(true);

		when(attributeDescriptor8.getQualifier()).thenReturn(QUALIFIER_8);
		when(attributeDescriptor8.getAttributeType()).thenReturn(collectionTypeModel);
		when(attributeDescriptor8.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor8)).thenReturn((Class)LinkedList.class);

		when(attributeDescriptor9.getQualifier()).thenReturn(QUALIFIER_9);
		when(attributeDescriptor9.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor9.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor9)).thenReturn((Class)LinkedList.class);

		when(attributeDescriptor10.getQualifier()).thenReturn(QUALIFIER_10);
		when(attributeDescriptor10.getAttributeType()).thenReturn(simpleTypeModel);
		when(attributeDescriptor10.getWritable()).thenReturn(true);
		when(attributeDescriptorModelHelperService.getAttributeClass(attributeDescriptor10)).thenReturn((Class)LinkedList.class);

		//
		when(cmsItemProperty1.getPk()).thenReturn(fromLong(1));
		when(cmsItemProperty2.getPk()).thenReturn(fromLong(12));
		when(modifiedEmbeddedProperty.getPk()).thenReturn(fromLong(123));

		when(cmsAdminItemService.createItem(anyObject())).thenAnswer(answer ->
		{

			final Class<? extends CMSItemModel> modelClass = (Class<? extends CMSItemModel>) answer.getArguments()[0];

			return modelClass.newInstance();
		});

		//
		when(modelService.getAttributeValue(modifiedCmsItemPartOfProperty, QUALIFIER_PART_0)).thenReturn("someString0");
		when(modelService.getAttributeValue(newCmsItemPartOfProperty1, QUALIFIER_PART_0)).thenReturn("someString1");
		when(modelService.getAttributeValue(newCmsItemPartOfProperty2, QUALIFIER_PART_0)).thenReturn("someString2");
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_0)).thenReturn(cmsItemProperty1);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_1)).thenReturn(modifiedCmsItemPartOfProperty);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_2, ENGLISH)).thenReturn(cmsItemProperty1);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_2, FRENCH)).thenReturn(cmsItemProperty2);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_3, ENGLISH)).thenReturn(modifiedCmsItemPartOfProperty);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_3, FRENCH)).thenReturn(null);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_4)).thenReturn(asList(cmsItemProperty1, cmsItemProperty2));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_5)).thenReturn(asList(newCmsItemPartOfProperty1));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_6, ENGLISH)).thenReturn(asList(cmsItemProperty1));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_6, FRENCH)).thenReturn(asList(cmsItemProperty2));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_7, ENGLISH)).thenReturn(asList(newCmsItemPartOfProperty2));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_7, FRENCH)).thenReturn(null);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_8))
		.thenReturn(asList(modifiedEmbeddedProperty, newEmbeddedProperty));
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_9)).thenReturn(cmsItemProperty1);
		when(modelService.getAttributeValue(cmsItem, QUALIFIER_10)).thenReturn(cmsItemProperty1);

		when(modelService.create(SubClass.class)).thenReturn(DefaultCMSItemConverterTest.SubClass.class.newInstance());

		when(uniqueItemIdentifierService.getItemData(any(ItemModel.class))).then(answer ->
		{
			final ItemModel item = (ItemModel) answer.getArguments()[0];
			if (item == cmsItem) {
				return ofNullable(getItemData("uuidMain"));
			} else if (item == modifiedCmsItemPartOfProperty) {
				return ofNullable(getItemData("uuidPartOf"));
			} else {
				return empty();
			}
		});

		when(uniqueItemIdentifierService.getItemModel(anyString(), anyObject())).then(answer ->
		{
			final String uuid = (String) answer.getArguments()[0];
			final Class<? extends ItemModel> modelClass = (Class<? extends ItemModel>) answer.getArguments()[1];

			if (uuid.equals("uuidMain") && modelClass == MainClass.class) {
				return ofNullable(cmsItem);
			} else if (uuid.equals("uuidPartOf") && modelClass == SubClass.class) {
				return ofNullable(modifiedCmsItemPartOfProperty);
			} else {
				return empty();
			}
		});

		//
		when(attributeStrategyConverter.getContentConverter(partOfAttributeDescriptor0)).thenReturn(
				partOfAttributeContentConverter0);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor0)).thenReturn(attributeContentConverter0);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor2)).thenReturn(attributeContentConverter2);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor4)).thenReturn(attributeContentConverter4);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor6)).thenReturn(attributeContentConverter6);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor8)).thenReturn(attributeContentConverter8);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor9)).thenReturn(attributeContentConverter9);
		when(attributeStrategyConverter.getContentConverter(attributeDescriptor10)).thenReturn(null);

		when(partOfAttributeContentConverter0.convertModelToData(Mockito.eq(partOfAttributeDescriptor0), Mockito.anyString()))
		.thenAnswer(answer ->
		{
			return answer.getArguments()[1];
		});
		when(partOfAttributeContentConverter0.convertDataToModel(Mockito.eq(partOfAttributeDescriptor0), Mockito.anyString()))
		.thenAnswer(answer ->
		{
			return answer.getArguments()[1];
		});


		when(attributeContentConverter0.convertModelToData(attributeDescriptor0, cmsItemProperty1)).thenReturn(uuid1);
		when(attributeContentConverter0.convertDataToModel(attributeDescriptor0, uuid1)).thenReturn(cmsItemProperty1);

		when(attributeContentConverter2.convertModelToData(attributeDescriptor2, cmsItemProperty1)).thenReturn(uuid1);
		when(attributeContentConverter2.convertDataToModel(attributeDescriptor2, uuid1)).thenReturn(cmsItemProperty1);

		when(attributeContentConverter2.convertModelToData(attributeDescriptor2, cmsItemProperty2)).thenReturn(uuid2);
		when(attributeContentConverter2.convertDataToModel(attributeDescriptor2, uuid2)).thenReturn(cmsItemProperty2);

		when(attributeContentConverter4.convertModelToData(attributeDescriptor4, cmsItemProperty1)).thenReturn(uuid1);
		when(attributeContentConverter4.convertDataToModel(attributeDescriptor4, uuid1)).thenReturn(cmsItemProperty1);

		when(attributeContentConverter4.convertModelToData(attributeDescriptor4, cmsItemProperty2)).thenReturn(uuid2);
		when(attributeContentConverter4.convertDataToModel(attributeDescriptor4, uuid2)).thenReturn(cmsItemProperty2);

		when(attributeContentConverter6.convertModelToData(attributeDescriptor6, cmsItemProperty1)).thenReturn(uuid1);
		when(attributeContentConverter6.convertDataToModel(attributeDescriptor6, uuid1)).thenReturn(cmsItemProperty1);

		when(attributeContentConverter6.convertModelToData(attributeDescriptor6, cmsItemProperty2)).thenReturn(uuid2);
		when(attributeContentConverter6.convertDataToModel(attributeDescriptor6, uuid2)).thenReturn(cmsItemProperty2);

		when(attributeContentConverter9.convertModelToData(attributeDescriptor9, cmsItemProperty1)).thenReturn(null);

		final Map<String, Object> modifiedEmbeddedPropertyRepresentation = new HashMap<>();
		modifiedEmbeddedPropertyRepresentation.put("someIdentifierKey", "someIdentifierValue");
		modifiedEmbeddedPropertyRepresentation.put("key1", "value1");
		modifiedEmbeddedPropertyRepresentation.put("key2", "value2");
		when(attributeContentConverter8.convertModelToData(attributeDescriptor8, modifiedEmbeddedProperty)).thenReturn(
				modifiedEmbeddedPropertyRepresentation);
		when(attributeContentConverter8.convertDataToModel(attributeDescriptor8, modifiedEmbeddedPropertyRepresentation))
		.thenReturn(modifiedEmbeddedProperty);

		final Map<String, Object> newEmbeddedPropertyRepresentation = new HashMap<>();
		newEmbeddedPropertyRepresentation.put("key1", "value3");
		newEmbeddedPropertyRepresentation.put("key2", "value4");
		when(attributeContentConverter8.convertModelToData(attributeDescriptor8, newEmbeddedProperty)).thenReturn(
				newEmbeddedPropertyRepresentation);
		when(attributeContentConverter8.convertDataToModel(attributeDescriptor8, newEmbeddedPropertyRepresentation)).thenReturn(
				newEmbeddedProperty);


		when(dateConverter.convert(any())).thenReturn("some-formatted-date");

		final ValidationErrors validationErrors = mock(ValidationErrors.class);
		when(validationErrorsProvider.getCurrentValidationErrors()).thenReturn(validationErrors);
		verifyZeroInteractions(attributeContentConverter9);

		doAnswer((invocationOnMock) -> {
			final Object[] args = invocationOnMock.getArguments();
			return ((Supplier<?>)args[0]).get();
		}).when(validatableService).execute(any());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testIfItemModelIsNull()
	{
		cmsItemConverter.convert((ItemModel) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIfMapIsNull() throws InstantiationException, IllegalAccessException
	{
		cmsItemConverter.convert((Map<String, Object>) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIfMapHasNoType() throws InstantiationException, IllegalAccessException
	{
		cmsItemConverter.convert(new HashMap<String, Object>());
	}


	@Test
	public void willConverterItemContainingAllCombinationsofLocalizedCollectionAndPartOf() throws IOException
	{
		assertThat(cmsItemConverter.convert(cmsItem), new JSONMatcher<Map<String, Object>>(
				"/cmsfacades/test/expectedSerializedItemForContentItemConverter.json"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void willConverterMapContainingAllCombinationsOfLocalizedCollectionAndPartOf() throws IOException,
	InstantiationException, IllegalAccessException
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(FALSE);
		//EXECUTE
		final ObjectMapper mapper = new ObjectMapper();
		final InputStream inputStream = getClass().getResourceAsStream(
				"/cmsfacades/test/expectedSerializedItemForContentItemConverter.json");
		final Map<String, Object> map = mapper.readValue(inputStream, HashMap.class);
		inputStream.close();

		final ItemModel converted = cmsItemConverter.convert(map);

		assertThat("the returned converted map was expected to be cmsITEM", converted, is(cmsItem));


		//preparing a map of key/Values of the root object
		verify(modelService, times(10)).setAttributeValue(eq(converted), propertyCator.capture(), valueCaptor.capture());
		final List<String> propertyValues = propertyCator.getAllValues();
		final List<Object> valueValues = valueCaptor.getAllValues();
		final Map<String, Object> captorMap = new HashMap<>();
		final Iterator<Object> valueIterator = valueValues.iterator();
		propertyValues.forEach(property ->
		{
			captorMap.put(property, valueIterator.next());
		});


		//assert on final POJO content

		assertThat(captorMap.get(QUALIFIER_0), is(cmsItemProperty1));
		assertThat(captorMap.get(QUALIFIER_1), is(modifiedCmsItemPartOfProperty));

		final Map<Locale, ItemModel> localized = (Map<Locale, ItemModel>) captorMap.get(QUALIFIER_2);
		assertThat("localizedMap was expected to have 2 entries", localized.size(), is(2));
		assertThat(localized.get(ENGLISH), is(cmsItemProperty1));
		assertThat(localized.get(FRENCH), is(cmsItemProperty2));

		//
		final Map<Locale, ItemModel> localizedOfPartOf = (Map<Locale, ItemModel>) captorMap.get(QUALIFIER_3);
		assertThat("localizedOfpartOf was expected to have 2 entries", localized.size(), is(2));
		assertThat(localizedOfPartOf.get(FRENCH), is((ItemModel) null));
		assertThat(localizedOfPartOf.get(ENGLISH), is(modifiedCmsItemPartOfProperty));
		//
		assertThat("collection was expected to contain cmsItemProperty1 and cmsItemProperty2",
				(Collection<ItemModel>) captorMap.get(QUALIFIER_4), contains(cmsItemProperty1, cmsItemProperty2));

		//
		final Collection<ItemModel> collectionPartOf = (Collection<ItemModel>) captorMap.get(QUALIFIER_5);
		assertThat("collectionPartOf was expected to have 1 entry", collectionPartOf.size(), is(1));
		final ItemModel collectionPartOf1 = collectionPartOf.iterator().next();
		assertThat("collectionPartOf1 should be of type SubClass",
				SubClass.class.isAssignableFrom(collectionPartOf1.getClass()), is(true));
		verify(modelService, times(1)).setAttributeValue(collectionPartOf1, QUALIFIER_PART_0, "someString1");

		//
		final Map<Locale, Collection<ItemModel>> localizedCollection = (Map<Locale, Collection<ItemModel>>) captorMap.get(QUALIFIER_6);
		assertThat("localizedCollection was expected to have 2 entries", localizedCollection.size(), is(2));
		assertThat(localizedCollection.get(ENGLISH), containsInAnyOrder(cmsItemProperty1));
		assertThat(localizedCollection.get(FRENCH), containsInAnyOrder(cmsItemProperty2));

		//
		final Map<Locale, Collection<ItemModel>> localizedCollectionOfPartOf = (Map<Locale, Collection<ItemModel>>) captorMap
				.get(QUALIFIER_7);
		assertThat("localizedCollectionOfPartOf was expected to have 2 entries", localizedCollectionOfPartOf.size(), is(2));
		assertThat(localizedCollectionOfPartOf.get(FRENCH), is((Collection<ItemModel>) null));
		final Collection<ItemModel> localizedCollectionOfPartOfEnglish = localizedCollectionOfPartOf.get(ENGLISH);
		assertThat("localizedCollectionOfPartOfEnglish was expected to have 1 entry", localizedCollectionOfPartOfEnglish.size(),
				is(1));
		final ItemModel englishPartOf = localizedCollectionOfPartOfEnglish.iterator().next();
		assertThat("localizedCollectionOfPartOfEnglish should be of type SubClass",
				CMSItemSubClass.class.isAssignableFrom(englishPartOf.getClass()), is(true));
		//englishPartOf should be populated with content from map
		verify(modelService, times(1)).setAttributeValue(englishPartOf, QUALIFIER_PART_0, "someString2");

		//
		assertThat((Collection<ItemModel>) captorMap.get(QUALIFIER_8), contains(modifiedEmbeddedProperty, newEmbeddedProperty));

		verify(modelService, times(2)).setAttributeValue(modifiedCmsItemPartOfProperty, QUALIFIER_PART_0, "someString0");


		//verify(modelService, never()).setAttributeValue(eq(cmsItem), eq(QUALIFIER_9), anyObject());
		verify(modelService, never()).setAttributeValue(eq(cmsItem), eq(QUALIFIER_10), anyObject());

		//ASSERT on persistence calls from BOTTOM to TOP
		verify(modelService, never()).save(Matchers.any(ItemModel.class));

		verify(cmsAdminItemService, times(1)).createItem(CMSItemSubClass.class);

		verify(cmsItemValidatorCreate, times(2)).validate(any(ItemModel.class));

	}

	@Test
	public void shouldFindModificationForSimpleAttributeType()
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(TRUE);
		final Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put(NAME, "value");
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(sourceMap);

		final boolean value = cmsItemConverter.initializeCloneComponentAttributeContext(NAME, "clone-test-data");

		assertThat(value, is(TRUE));
	}

	@Test
	public void shouldFindModificationForComplexAttributeType()
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(TRUE);
		final Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put(NAME, new HashMap<>());
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(sourceMap);

		final Map<String, Object> inputMap = new HashMap<>();
		final Map<String, String> localizedNames = new HashMap<>();
		localizedNames.put("en", "name");
		localizedNames.put("fr", "nom");
		inputMap.put(NAME, localizedNames);

		final boolean value = cmsItemConverter.initializeCloneComponentAttributeContext(NAME, inputMap);

		assertThat(value, is(TRUE));
	}

	@Test
	public void shouldNotFindModificationForUidAttribute()
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(TRUE);
		final Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put(UID, "comp_1234");
		when(cloneComponentContextProvider.findItemForKey(SESSION_CLONE_COMPONENT_SOURCE_MAP)).thenReturn(sourceMap);

		final boolean value = cmsItemConverter.initializeCloneComponentAttributeContext(UID, "clone_9876");

		assertThat(value, is(FALSE));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotInitializeCloneComponentAttributeContextForNonComponentCloningFlow()
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(FALSE);
		cmsItemConverter.initializeCloneComponentAttributeContext(UID, "clone_9876");
	}

	@Test
	public void shouldCloneComponentFromRepresentation() throws CMSItemNotFoundException
	{
		final AbstractCMSComponentModel sourceComponentModel = mock(AbstractCMSComponentModel.class);
		final AbstractCMSComponentModel cloneComponentModel = mock(AbstractCMSComponentModel.class);
		when(uniqueItemIdentifierService.getItemModel(anyString(), any())).thenReturn(Optional.of(sourceComponentModel));
		when(componentCloningStrategy.clone(sourceComponentModel, Optional.empty(), Optional.empty()))
		.thenReturn(cloneComponentModel);
		doReturn(new HashMap<>()).when(cmsItemConverter).convert(sourceComponentModel);
		doReturn(new HashMap<>()).when(cmsItemConverter).convert(cloneComponentModel);

		cmsItemConverter.getCloneModelFromRepresentation("#uuid_1234#");

		verify(componentCloningStrategy).clone(sourceComponentModel, Optional.empty(), Optional.empty());
		verify(cloneComponentContextProvider, times(2)).initializeItem(any());
	}

	@Test
	public void shouldNotCloneComponentFromRepresentationWithSessionAlreadyInitialized() throws CMSItemNotFoundException
	{
		when(cloneComponentContextProvider.isInitialized()).thenReturn(TRUE);

		cmsItemConverter.getCloneModelFromRepresentation("#uuid_1234#");

		verifyZeroInteractions(componentCloningStrategy);
		verify(cloneComponentContextProvider, times(0)).initializeItem(any());
		verify(cloneComponentContextProvider).findItemForKey(SESSION_CLONE_COMPONENT_CLONE_MODEL);
	}

	@Test
	public void shouldGetItemModelFromCloneComponentRepresentation()
	{
		final String sourceUuid = "#uuid_1234#";
		final Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(FIELD_COMPONENT_UUID, sourceUuid);
		inputMap.put(FIELD_CLONE_COMPONENT, TRUE);
		doReturn(new AbstractCMSComponentModel()).when(cmsItemConverter).getCloneModelFromRepresentation(sourceUuid);

		cmsItemConverter.getItemModelFromRepresentation(inputMap);

		verify(cmsItemConverter).getCloneModelFromRepresentation(sourceUuid);
		verify(cmsItemConverter).isCloneComponentFlow(inputMap);
	}

	@Test
	public void shouldCreateItemModelFromRepresentation()
	{
		final Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(ITEMTYPE, _TYPECODE);
		inputMap.put(FIELD_COMPONENT_UUID, null);
		inputMap.put(FIELD_CLONE_COMPONENT, FALSE);

		cmsItemConverter.getItemModelFromRepresentation(inputMap);

		verify(modelService).create(any(Class.class));
		verify(modelService).initDefaults(any());
	}

	@Test
	public void isCloneComponentFlowShouldReturnFalseForNonCloneableComponents()
	{
		final Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UID, "comp_1234");

		assertThat(cmsItemConverter.isCloneComponentFlow(inputMap), is(FALSE));
	}

	@Test
	public void isCloneComponentFlowShouldReturnTrueForCloneableComponents()
	{
		final String sourceUuid = "#uuid_1234#";
		final Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(FIELD_COMPONENT_UUID, sourceUuid);
		inputMap.put(FIELD_CLONE_COMPONENT, TRUE);

		assertThat(cmsItemConverter.isCloneComponentFlow(inputMap), is(TRUE));
	}

}
