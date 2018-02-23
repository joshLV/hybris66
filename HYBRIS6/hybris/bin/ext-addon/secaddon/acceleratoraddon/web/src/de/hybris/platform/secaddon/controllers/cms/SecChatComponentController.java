package de.hybris.platform.secaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.secaddon.model.components.SecChatComponentModel;

import de.hybris.platform.util.localization.Localization;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static de.hybris.platform.secaddon.controllers.SecaddonControllerConstants.Cms.SecChatComponent;


@Controller("SecChatComponentController")
@RequestMapping(value = SecChatComponent)
public class SecChatComponentController extends AbstractCMSAddOnComponentController<SecChatComponentModel>
{
	public static final String SECADDON_CHAT_TITLE_TEXT = "secaddon.chat.title.text";
	public static final String SECADDON_CHAT_TITLE_VIDEO = "secaddon.chat.title.video";
	public static final String CHAT_ADDON_FRAGMENT_PATH = "addon:/secaddon/cms/secchatwindow";

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;

	@Override
	protected void fillModel(HttpServletRequest request, Model model, SecChatComponentModel component)
	{
		model.addAttribute("customerName",customerFacade.getCurrentCustomer().getName());
		model.addAttribute("customerEmail",customerFacade.getCurrentCustomer().getUid());
		model.addAttribute("chatQueue", component.getChatQueue());
		model.addAttribute("chatEcfModulePath", component.getChatEcfModulePath());
		model.addAttribute("chatCctrUrl", component.getChatCctrUrl());
		model.addAttribute("chatBootstrapUrl", component.getChatBootstrapUrl());
		model.addAttribute("currentLanguage", storeSessionFacade.getCurrentLanguage().getIsocode());
		model.addAttribute("textChatTitle", Localization.getLocalizedString(SECADDON_CHAT_TITLE_TEXT));
		model.addAttribute("videoChatTitle",Localization.getLocalizedString(SECADDON_CHAT_TITLE_VIDEO));
		model.addAttribute("videoChatEnabled", component.getVideoChatEnabled());
	}
	/**
	 * Method for getting fragment's JSP renderer as response on GET or POSTs request
	 *
	 * @param model
	 * @param allRequestParams
	 *           all request parameters
	 * @return fragment with populated data and renderer
	 */
	@RequestMapping(value = "/chatFragment", method =
			{ RequestMethod.POST, RequestMethod.GET })
	public String getChatFragment(final Model model, @RequestParam final Map<String, String> allRequestParams)
	{
		return CHAT_ADDON_FRAGMENT_PATH;
	}

}
