import * as angular from 'angular';

import {ISharedDataService, SharedDataServiceInterfaceModule} from './core/services/sharedDataServiceInterface';
import {TestModeService} from './services/testModeService';
import './core/services/ICatalogService';
import './core/services/IContextualMenuButton';
import './core/services/IDecorator';
import './core/services/IFeatureService';

export {
	ISharedDataService
};

/*
    Note: For backward compatibility legacy services define their own module. However, after the introduction
    of TypeScript, angular modules should only be created on the index.ts file of a major module.
*/
export const SmarteditCommonsModule = angular
	.module('smartedit.commons', [SharedDataServiceInterfaceModule])
	.service('testModeService', TestModeService)
	.name;

