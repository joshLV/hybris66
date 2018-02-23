import * as angular from 'angular';
declare module 'smarteditcontainer' {


export * from './services';


/**
 * @ngdoc overview
 * @name administration
 *
 * @description
 * # The administration module
 *
 * The administration module provides services to display and manage configurations
 * that point to web service and the value property contains the URI of the web service or data.
 *
 */










/// <reference types="angular" />


/**
 * @ngdoc service
 * @name sharedDataServiceModule.sharedDataService
 *
 * @description
 * The Shared Data Service is used to store data that is to be shared between the SmartEdit application and the
 * SmartEdit container. It uses the {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to share data between
 * the SmartEdit application and the container. It uses the gateway ID "sharedData".
 *
 * The Shared Data Service extends the {@link sharedDataServiceInterfaceModule.SharedDataServiceInterface
 * SharedDataServiceInterface}.
 *
 */
export class SharedDataService implements ISharedDataService {
    gatewayId: string;
    private $q;
    private storage;
    constructor($q: angular.IQService, gatewayProxy: any);
    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Retrieve the data for the given key.
     *
     * @param {String} key The key of the data to retrieve
     */
    get(name: string): any;
    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Store data for the given key.
     *
     * @param {String} key The key of the data to store
     * @param {String} value The value of the data to store
     */
    set(name: string, value: any): void;
    update(name: string, modifyingCallback: (oldValue: any) => any): angular.IPromise<void>;
}
/**
 * @ngdoc overview
 * @name pageToolMenuModule
 * @description
 * # The sharedDataServiceModule
 *
 * The Shared Data Service Module provides a service used to store and retrieve data using a key. The data can be shared
 * between the SmartEdit application and SmartEdit container.
 *
 */




}