import * as angular from 'angular';
declare module 'smartedit' {







/**
 * @ngdoc overview
 * @name sharedDataServiceModule
 * @description
 * # The sharedDataServiceModule
 *
 * The Shared Data Service Module provides a service used to store and retrieve data using a key. The data can be shared
 * between the SmartEdit application and SmartEdit container.
 *
 */

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
    readonly GATEWAY_ID: string;
    gatewayId: string;
    constructor(gatewayProxy: any);
    get(key: string): void;
    set(key: string, value: string): void;
    update(key: string, modifyingCallback: (oldValue: any) => any): void;
}


}