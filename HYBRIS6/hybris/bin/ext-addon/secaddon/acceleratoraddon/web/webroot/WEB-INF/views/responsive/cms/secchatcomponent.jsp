<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<%-- JS configuration --%>
<script type="text/javascript">
    var ecfInitConfig;
    var ecfSession;

    var CHAT_CONFIG = {};
    CHAT_CONFIG.customerName = '${customerName}';
    CHAT_CONFIG.customerEmail = '${customerEmail}';
    CHAT_CONFIG.controllerPath = '${chatControllerPath}';
    CHAT_CONFIG.queue = '${chatQueue}';
    CHAT_CONFIG.modulePath = '${chatEcfModulePath}';
    CHAT_CONFIG.cctrUrl = '${chatCctrUrl}';
    CHAT_CONFIG.bootstrapUrl = '${chatBootstrapUrl}';
    CHAT_CONFIG.currentLanguage = '${currentLanguage}';

    var isChrome = !!window.chrome && !!window.chrome.webstore;
    var isFirefox = typeof InstallTrigger !== 'undefined';
</script>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


<script
        id="sap-ui-bootstrap" type="text/javascript"
        src="${chatBootstrapUrl}"
        data-sap-ui-libs="sap.ui.commons,sap.m"
        data-sap-ui-noConflict="true"
        data-sap-ui-theme="sap_bluecrystal" defer>
</script>

<script type="text/javascript">
   $( document ).ready(function() {
	sap.ui.getCore().attachInit(function () {
            //Register the module path
            jQuery.sap.registerModulePath("sap.ecf", CHAT_CONFIG.modulePath);
            // Load minified, uglified compressed ecf library
            jQuery.sap.require("sap.ecf.sap-ecf");
            //Interaction - controls
            jQuery.sap.require("sap.ecf.controls.Interaction");
            widgetInteraction = new sap.ecf.controls.Interaction();
            jQuery.sap.require("sap.ecf.core.Session");

            //Create a configuration object
            ecfInitConfig = new sap.ecf.models.InitConfig();

            // Set Anonymous "Visitor" authentication
            ecfInitConfig.setAuthenticationType("Anonymous");

            //Set the URL for SAP ECF Server
            ecfInitConfig.setCctrUrl(CHAT_CONFIG.cctrUrl);

            //Set language
            ecfInitConfig.setLocale(CHAT_CONFIG.currentLanguage);

            //Session object - core
            ecfSession = sap.ecf.core.Session.getInstance();

            function ecfConnect() {
                $(document).ready(function() {
                    ecfSession.connect(ecfInitConfig);
                });
            };

            if (!ecfSession._isConnected) {
                ecfConnect();
            }
        });
    });
</script>

<jsp:include page="secchatwindow.jsp">
    <jsp:param name="chatType" value="text"/>
    <jsp:param name="chatBtnTitle" value="${textChatTitle}"/>
</jsp:include>

<c:if test="${videoChatEnabled}">
    <jsp:include page="secchatwindow.jsp">
        <jsp:param name="chatType" value="video"/>
        <jsp:param name="chatBtnTitle" value="${videoChatTitle}"/>
    </jsp:include>
</c:if>

<div class="open-chat-popup secchat-wrapper" id="chatinfo">
</div>
<div id="chat-wrapper">
  <div id="chat-popup">
    <div style="height: 750px; width: 100%;" id="interactions">
    </div>
  </div>
</div>
