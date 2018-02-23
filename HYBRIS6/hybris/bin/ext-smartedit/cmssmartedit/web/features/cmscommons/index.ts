import * as angular from 'angular';

import {AssetsService, AssetsServiceModule} from './services/assetsService';

export {
	AssetsService
};

/*
    Note: For backward compatibility legacy services define their own module. However, after the introduction
    of TypeScript, angular modules should only be created on the index.ts file of a major module.
*/
export const CmsCommonsModule = angular
	.module('cms.commons', [AssetsServiceModule])
	.name;
