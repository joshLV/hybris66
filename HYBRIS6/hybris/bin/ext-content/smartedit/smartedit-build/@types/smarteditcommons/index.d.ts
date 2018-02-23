import * as angular from 'angular';
declare module 'smarteditcommons' {
/// <reference types="angular" />
export interface ICatalog {
    name: string;
    uuid?: string;
}
export interface ICatalogService {
    /**
     * list catalogs for a given site
     *
     * @param {=string} siteId the site identifier
     *
     * @return {ng.IPromise<ICatalog[]>} list of catalogs for the given site
     */
    getCatalogs(siteId?: string): ng.IPromise<ICatalog[]>;
}

export interface IContextualMenuButton {
    action?: {
        template: string;
    };
    i18nKey?: string;
    callbacks?: any;
    displayClass?: string;
    displayIconClass?: string;
    displaySmallIconClass?: string;
    permissions?: string[];
    templateUrl?: string;
    regexpKeys: string[];
    key: string;
    nameI18nKey: string;
    condition?(configuration: any): any;
    callback(configuration?: any, $event?: any): void;
}

/// <reference types="angular" />
export interface IDecorator {
    key: string;
    permissions?: string[];
    nameI18nKey: string;
    displayCondition?(componentType: string, componentId: string): ng.IPromise<number>;
}



export interface IFeatureService {
    addContextualMenuButton(btn: IContextualMenuButton): void;
    register(_interface: any): void;
    addDecorator(decorator: IDecorator): void;
    addToolbarItem(toolbar: any): void;
}

/**
 * @ngdoc service
 * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface
 *
 * @description
 * Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
export interface ISharedDataService {
    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Get the data for the given key.
     *
     * @param {String} key The key of the data to fetch
     */
    get(key: string): any;
    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Set data for the given key.
     *
     * @param {String} key The key of the data to set
     * @param {String} value The value of the data to set
     */
    set(key: string, value: string): any;
    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#update
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Convenience method to retrieve and modify on the fly the content stored under a given key
     *
     * @param {String} key The key of the data to store
     * @param {Function} modifyingCallback callback fed with the value stored under the given key. The callback must return the new value of the object to update.
     */
    update(key: string, modifyingCallback: (oldValue: any) => any): any;
}










/// <reference types="angular" />

export class TestModeService {
    private $injector;
    private readonly TEST_KEY;
    constructor($injector: angular.auto.IInjectorService);
    isE2EMode(): boolean;
}

}