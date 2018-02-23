import * as angular from 'angular';

describe('ysmarteditmodulecommons - some test suite with TypeScript', () => {

	beforeEach(() => {
		angular.mock.module("ysmarteditmodule");
	});

	it('will assert that true equals true', () => {
		expect(true).toBe(true);
	});
});
