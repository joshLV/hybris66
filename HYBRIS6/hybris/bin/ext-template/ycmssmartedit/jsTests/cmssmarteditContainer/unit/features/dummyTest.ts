import * as angular from 'angular';

describe('cmssmarteditContainer - some test suite with TypeScript', () => {

	beforeEach(() => {
		angular.mock.module("ycmssmarteditModule");
	});

	it('will assert that true equals true', () => {
		expect(true).toBe(true);
	});
});

