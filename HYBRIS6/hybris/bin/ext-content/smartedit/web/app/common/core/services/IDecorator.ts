export interface IDecorator {
	key: string;
	permissions?: string[];
	nameI18nKey: string;
	displayCondition?(componentType: string, componentId: string): ng.IPromise<number>;
}
