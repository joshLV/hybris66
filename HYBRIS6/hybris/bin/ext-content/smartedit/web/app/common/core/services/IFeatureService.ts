import {IContextualMenuButton} from './IContextualMenuButton';
import {IDecorator} from './IDecorator';

export interface IFeatureService {
	addContextualMenuButton(btn: IContextualMenuButton): void;
	register(_interface: any): void;
	addDecorator(decorator: IDecorator): void;
	addToolbarItem(toolbar: any): void;
}
