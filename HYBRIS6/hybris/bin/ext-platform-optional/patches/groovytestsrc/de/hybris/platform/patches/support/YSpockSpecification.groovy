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
package de.hybris.platform.patches.support

import de.hybris.platform.core.PK
import de.hybris.platform.core.Registry
import de.hybris.platform.jalo.ConsistencyCheckException
import de.hybris.platform.jalo.JaloItemNotFoundException
import de.hybris.platform.jalo.JaloSession
import de.hybris.platform.jalo.JaloSystemException
import de.hybris.platform.jalo.c2l.C2LManager
import de.hybris.platform.jalo.c2l.Currency
import de.hybris.platform.jalo.c2l.Language
import de.hybris.platform.jalo.order.price.PriceFactory
import de.hybris.platform.jalo.user.UserManager
import de.hybris.platform.testframework.HybrisJUnit4Test
import de.hybris.platform.testframework.RunListeners
import de.hybris.platform.testframework.runlistener.*
import de.hybris.platform.util.Config
import org.apache.log4j.Logger
import org.junit.runner.RunWith
import spock.lang.Specification

import static junit.framework.Assert.assertEquals

/**
 * Hybris spock specification.
 * Code from {@link HybrisJUnit4Test}
 *
 */
@RunWith(YSpockHybrisRunner.class)
@RunListeners([ LangReferenceRemoverRunListener.class,
        ApplicationContextCheckRunListener.class,
        TableSizeCheckRunListener.class, TransactionRunListener.class,
        ItemCreationListener.class, C2LSetupRunListener.class,
        LogRunListener.class, ClassLoaderOverviewRunListener.class,
        PlatformRunListener.class, ResetMockitoRunListener.class,
        PlatformConfigurationCheckRunListener.class,
        VMBlockTimeRunListener.class, OpenDBConnectionRunListener.class,
        MemoryOverviewRunListener.class ])
//CHECKSTYLE:OFF
@SuppressWarnings("PMD")
class YSpockSpecification extends Specification
{
    /** Used logger instance. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(HybrisJUnit4Test.class);
    /** Reference to current session. */
    protected JaloSession jaloSession;
    /** Reference to default language. */
    @Deprecated
    protected Language defaultLanguage;
    private PriceFactory priceFactory;
    private PK userPK;
    private String userUid;
    private Date userTS;
    /**
     * returns true if property "test.intense.checks" is activated if active, then tests like -
     * ApplicationContextCheckRunListener (compares all beans before and after testrun) - TableSizeCheckRunListner (
     * compares ALL table sizes (SELECT COUNT) before and after testrun) are being executed
     */
    public static boolean intenseChecksActivated()
    {
        return Registry.hasCurrentTenant() && Config.getBoolean("junit.intense.checks", false);
    }
    /**
     * Sets the current session and default language.
     *
     * @throws JaloSystemException
     *            there was a security problem while creating the session
     */
    def setup()
    {
        // jalo session is handled by PlatformRunListener -> just grab it
        junit.framework.Assert.assertTrue(JaloSession.hasCurrentSession());
        jaloSession = JaloSession.getCurrentSession();
        userPK = jaloSession.getUser().getPK();
        userUid = jaloSession.getUser().getUid();
        userTS = jaloSession.getUser().getModificationTime();
        priceFactory = JaloSession.getCurrentSession().getPriceFactory();
    }

    def cleanup()
    {
        if (!UserManager.getInstance().getAnonymousCustomer().equals(jaloSession.getUser()))
        {
            if (jaloSession.getUser() != null)
            {
                LOG.warn("session user(" + jaloSession.getUser() + ") has changed from ("
                        + UserManager.getInstance().getAnonymousCustomer() + ") to (" + jaloSession.getUser()+")");
            }
        }
        else
        {
            try
            {
                if (!userPK.equals(jaloSession.getUser().getPK()))
                {
                    throw new IllegalStateException("session user(" + jaloSession.getUser() + ") PK has changed from (" + userPK
                            + ") to (" + jaloSession.getUser().getPK()+")");
                }
                if (!userUid.equals(jaloSession.getUser().getUid()))
                {
                    throw new IllegalStateException("session user(" + jaloSession.getUser() + ") uid has changed from (" + userUid
                            + ") to (" + jaloSession.getUser().getUid()+")");
                }
                if (!userTS.equals(jaloSession.getUser().getModificationTime()))
                {
                    LOG.warn("session user(" + jaloSession.getUser() + ") modified time has changed from (" + userTS + ") to ("
                            + jaloSession.getUser().getModificationTime()+")");
                }
            }
            catch (final Exception e)
            {
                LOG.error("#####################################################################");
                LOG.error("Test " + this.getClass().getSimpleName() + " has interefered session user  , " + e.getMessage());
            }
        }
        // XXX: hack: switch back session user in case a unit test changed it
        jaloSession.setUser(UserManager.getInstance().getAnonymousCustomer());
        //
        jaloSession = null;
        // jalo session will be de-activated by PlatformRunListener
        assertEquals("If you set jaloSession.priceFactory it need to be cleaned after test was performed.", priceFactory,
                JaloSession.getCurrentSession().getPriceFactory());
    }
    /**
     * Gets or creates (if not existent) language to given iso code.
     *
     * @param isoCode
     *           iso code of language
     * @return language to given iso code
     * @throws JaloSystemException
     *            thrown if language will be created but still exists
     */
    public static Language getOrCreateLanguage(final String isoCode) throws JaloSystemException
    {
        Language ret = null;
        try
        {
            ret = C2LManager.getInstance().getLanguageByIsoCode(isoCode);
        }
        catch (final JaloItemNotFoundException e)
        {
            try
            {
                ret = C2LManager.getInstance().createLanguage(isoCode);
            }
            catch (final ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }
    /**
     * Gets or creates (if not existent) currency to given iso code.
     *
     * @param isoCode
     *           iso code of currency
     * @return Currency to given iso code
     * @throws JaloSystemException
     *            thrown if currency will be created but still exists
     */
    public static Currency getOrCreateCurrency(final String isoCode) throws JaloSystemException
    {
        Currency ret = null;
        try
        {
            ret = C2LManager.getInstance().getCurrencyByIsoCode(isoCode);
        }
        catch (final JaloItemNotFoundException e)
        {
            try
            {
                ret = C2LManager.getInstance().createCurrency(isoCode);
            }
            catch (final ConsistencyCheckException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        return ret;
    }
}
//CHECKSTYLE:ON