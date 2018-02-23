/**
 *
 */
package de.hybris.platform.personalizationservices.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.impl.FlexibleSearchRetentionItemsProvider;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;



public class CxCustomerSegmentRetentionRuleIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String RULE_CODE = "cxCustomerSegmentRetentionRule";
	private static final String USER1 = "p1@hybris.com";
	private static final String USER2 = "p5@hybris.com";
	private static final String SEGMENT1 = "segment1";
	private static final String SEGMENT2 = "segment2";


	@Resource
	ModelService modelService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	private FlexibleSearchRetentionItemsProvider itemProvider;

	@Before
	public void setup() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importData(new ClasspathImpExResource("/personalizationservices/test/testdata_consent.impex", "UTF-8"));
		importData(new ClasspathImpExResource("/impex/essentialdata_personalizationservices_cron.impex", "UTF-8"));

		itemProvider = getItemProvider();
	}

	private FlexibleSearchRetentionItemsProvider getItemProvider()
	{
		final FlexibleSearchRetentionRuleModel example = new FlexibleSearchRetentionRuleModel();
		example.setCode(RULE_CODE);
		final FlexibleSearchRetentionRuleModel ruleModel = flexibleSearchService.getModelByExample(example);

		final FlexibleSearchRetentionItemsProvider provider = new FlexibleSearchRetentionItemsProvider(ruleModel);
		provider.setFlexibleSearchService(flexibleSearchService);
		provider.setBatchSize(100);
		return provider;
	}

	@Test
	public void test()
	{
		//when
		final List<ItemToCleanup> itemsToCleanup = itemProvider.nextItemsForCleanup();

		//then
		assertNotNull(itemsToCleanup);
		assertEquals(4, itemsToCleanup.size());

		assertUserToSegmentEquals(USER1, SEGMENT1, itemsToCleanup.get(0));
		assertUserToSegmentEquals(USER1, SEGMENT2, itemsToCleanup.get(1));
		assertUserToSegmentEquals(USER2, SEGMENT1, itemsToCleanup.get(2));
		assertUserToSegmentEquals(USER2, SEGMENT2, itemsToCleanup.get(3));
	}

	private void assertUserToSegmentEquals(final String expectedUser, final String expectedSegment, final ItemToCleanup item)
	{
		assertEquals(CxUserToSegmentModel._TYPECODE, item.getItemType());

		final CxUserToSegmentModel model = modelService.get(item.getPk());
		assertEquals(expectedUser, model.getUser().getUid());
		assertEquals(expectedSegment, model.getSegment().getCode());
	}

}
