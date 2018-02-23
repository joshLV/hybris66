export interface IContextualMenuButton {
	action?: {template: string};
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
