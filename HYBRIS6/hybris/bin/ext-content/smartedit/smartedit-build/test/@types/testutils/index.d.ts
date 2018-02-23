
declare namespace AngularUnitTestHelper {
	export interface IAngularUnitTestBuilder {
		and: any;
		service(serviceName: string): any;
		controller(serviceName: string): any;
		mock(serviceName: string, functionName: string): IAngularUnitTestBuilder;
		mockConstant(constantName: string, value: string): IAngularUnitTestBuilder;
	}

	export function prepareModule(moduleName: string): IAngularUnitTestBuilder;
}

declare var testutils: any;

declare namespace jasmine {
	interface Matchers<T> {
		toBeEmptyFunction(): boolean;
		toEqualData(expectedData: any[]): boolean;
	}