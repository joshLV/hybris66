import * as angular from 'angular';

describe('cmssmartedit - some test suite with TypeScript', () => {
	let ycmsSampleService: any;

	beforeEach(() => {
		angular.mock.module("ycmssmarteditModule");
		angular.mock.inject((_ycmsSampleService_: any) => {
			ycmsSampleService = _ycmsSampleService_;
		});
	});

	it('will assert that ycmsSampleService is defined', () => {
		expect(ycmsSampleService).toBeDefined();
	});
});
