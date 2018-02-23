import * as angular from 'angular';

const ycmssmarteditModule = angular
	.module('ycmssmarteditModule', ['ycmssmartedit/cmssmarteditTemplates'])
	.service('ycmsSampleService', () => {
		console.log('ycmsSampleService');
	});

export default ycmssmarteditModule;
