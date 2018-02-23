import * as angular from 'angular';

import {TrashedPageService} from '../services/pages/trashedPageService';
export const trashedPageModule = angular
	.module('trashedPageModule', [])
	.service('trashedPageService', TrashedPageService);
