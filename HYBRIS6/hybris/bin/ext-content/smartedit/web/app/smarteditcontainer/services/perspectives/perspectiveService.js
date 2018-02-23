/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
angular.module('perspectiveServiceModule', [
        'featureServiceModule',
        'functionsModule',
        'perspectiveServiceInterfaceModule',
        'gatewayProxyModule',
        'iFrameManagerModule',
        'storageServiceModule',
        'eventServiceModule',
        'crossFrameEventServiceModule',
        'permissionServiceModule',
        'seConstantsModule'
    ])
    .factory('perspectiveService', function(
        $log,
        $rootScope,
        $q,
        extend,
        isBlank,
        uniqueArray,
        systemEventService,
        Perspective,
        PerspectiveServiceInterface,
        featureService,
        gatewayProxy,
        iFrameManager,
        storageService,
        crossFrameEventService,
        NONE_PERSPECTIVE,
        ALL_PERSPECTIVE,
        EVENTS,
        EVENT_PERSPECTIVE_CHANGED,
        EVENT_PERSPECTIVE_UNLOADING,
        EVENT_PERSPECTIVE_ADDED,
        EVENT_PERSPECTIVE_REFRESHED,
        permissionService) {

        // Constants
        var PERSPECTIVE_COOKIE_NAME = "smartedit-perspectives";

        var INITIAL_SWITCHTO_ARG = 'INITIAL_SWITCHTO_ARG';

        var data = {
            activePerspective: undefined,
            previousPerspective: undefined,
            previousSwitchToArg: INITIAL_SWITCHTO_ARG
        };

        var perspectives = [];

        var PerspectiveService = function() {
            this.gatewayId = "perspectiveService";
            gatewayProxy.initForService(this, ['register', 'switchTo', 'hasActivePerspective', 'isEmptyPerspectiveActive', 'selectDefault', 'refreshPerspective']);

            this._addDefaultPerspectives();
            this._registerEventHandlers();
        };

        PerspectiveService = extend(PerspectiveServiceInterface, PerspectiveService);

        PerspectiveService.prototype._addDefaultPerspectives = function() {
            this.register({
                key: NONE_PERSPECTIVE,
                nameI18nKey: 'se.perspective.none.name',
                descriptionI18nKey: 'se.perspective.none.description'
            });

            this.register({
                key: ALL_PERSPECTIVE,
                nameI18nKey: 'se.perspective.all.name',
                descriptionI18nKey: 'se.perspective.all.description'
            });
        };

        PerspectiveService.prototype._registerEventHandlers = function() {
            systemEventService.registerEventHandler(EVENTS.EXPERIENCE_UPDATE, this._clearPerspectiveFeatures.bind(this));
            systemEventService.registerEventHandler(EVENTS.LOGOUT, this._onLogoutPerspectiveCleanup.bind(this));
            systemEventService.registerEventHandler(EVENTS.AUTHORIZATION_SUCCESS, this._clearPerspectiveFeatures.bind(this));
        };

        PerspectiveService.prototype._validate = function(configuration) {
            if (isBlank(configuration.key)) {
                throw new Error("perspectiveService.configuration.key.error.required");
            }
            if (isBlank(configuration.nameI18nKey)) {
                throw new Error("perspectiveService.configuration.nameI18nKey.error.required");
            }
            if ([NONE_PERSPECTIVE, ALL_PERSPECTIVE].indexOf(configuration.key) === -1 && (isBlank(configuration.features) || configuration.features.length === 0)) {
                throw new Error("perspectiveService.configuration.features.error.required");
            }
        };

        PerspectiveService.prototype._findByKey = function(key) {
            return perspectives.find(function(persp) {
                return persp.key === key;
            });
        };

        PerspectiveService.prototype._fetchAllFeatures = function(perspective, holder) {
            if (!holder) {
                holder = [];
            }

            if (perspective.key === ALL_PERSPECTIVE) {
                uniqueArray(holder, (featureService.getFeatureKeys() || []));
            } else {
                uniqueArray(holder, (perspective.features || []));

                (perspective.perspectives || []).forEach(function(perspectiveKey) {
                    var nestedPerspective = this._findByKey(perspectiveKey);
                    if (nestedPerspective) {
                        this._fetchAllFeatures(nestedPerspective, holder);
                    } else {
                        $log.debug("nested perspective " + perspectiveKey + " was not found in the registry");
                    }
                }.bind(this));
            }
        };

        PerspectiveService.prototype.register = function(configuration) {
            this._validate(configuration);

            var perspective = this._findByKey(configuration.key);

            if (!perspective) {
                perspective = new Perspective(configuration.nameI18nKey, [], true); //constructor will be modified once dependant code is refactored
                perspective.key = configuration.key;
                perspective.descriptionI18nKey = configuration.descriptionI18nKey;
                perspectives.push(perspective);
            }

            perspective.features = uniqueArray(perspective.features || [], configuration.features || []);
            perspective.perspectives = uniqueArray(perspective.perspectives || [], configuration.perspectives || []);
            perspective.permissions = uniqueArray(perspective.permissions || [], configuration.permissions || []);

            systemEventService.sendAsynchEvent(EVENT_PERSPECTIVE_ADDED);
        };

        PerspectiveService.prototype.getPerspectives = function() {
            var promises = [];

            perspectives.forEach(function(perspective) {
                var promise;

                if (perspective.permissions.length > 0) {
                    promise = permissionService.isPermitted([{
                        names: perspective.permissions
                    }]);
                } else {
                    promise = $q.when(true);
                }

                promises.push(promise);
            });

            return $q.all(promises).then(function(results) {
                return perspectives.filter(function(perspective, index) {
                    return results[index];
                });
            }.bind(this));
        };

        PerspectiveService.prototype.hasActivePerspective = function() {
            return Boolean(data.activePerspective);
        };

        PerspectiveService.prototype.getActivePerspective = function() {
            return data.activePerspective ? angular.copy(this._findByKey(data.activePerspective.key)) : null;
        };

        PerspectiveService.prototype.isEmptyPerspectiveActive = function() {
            return (!!data.activePerspective && data.activePerspective.key === NONE_PERSPECTIVE);
        };

        PerspectiveService.prototype._enableFeature = function(featureKey) {
            return featureService.getFeatureProperty(featureKey, "permissions").then(function(permissionNames) {
                if (!Array.isArray(permissionNames)) {
                    permissionNames = [];
                }
                return permissionService.isPermitted([{
                    names: permissionNames
                }]).then(function(allowCallback) {
                    if (allowCallback) {
                        featureService.enable(featureKey);
                    }
                });
            });
        };


        /**
         * Takes care of sending EVENT_PERSPECTIVE_UNLOADING when perspectives change.
         *
         * This function tracks the "key" argument in calls to switchTo(..) function in order to detect when a
         * perspective is being switched. Due to the implementation of clearActivePerspective() it is not really
         * clear in switchTo(...) when a perspective is actually being changed, or just reloaded.
         */
        PerspectiveService.prototype._handleUnloadEvent = function(nextPerspectiveKey) {
            if (nextPerspectiveKey !== data.previousSwitchToArg && data.previousSwitchToArg !== INITIAL_SWITCHTO_ARG) {
                crossFrameEventService.publish(EVENT_PERSPECTIVE_UNLOADING, data.previousSwitchToArg);
            }
            data.previousSwitchToArg = nextPerspectiveKey;
        };

        PerspectiveService.prototype.switchTo = function(key) {
            if (!this._changeActivePerspective(key)) {
                iFrameManager.hideWaitModal();
                return;
            }

            this._handleUnloadEvent(key);

            iFrameManager.showWaitModal();
            var featuresFromPreviousPerspective = [];
            if (data.previousPerspective) {
                this._fetchAllFeatures(data.previousPerspective, featuresFromPreviousPerspective);
            }
            var featuresFromNewPerspective = [];
            this._fetchAllFeatures(data.activePerspective, featuresFromNewPerspective);

            //deactivating any active feature not belonging to either the perspective or one of its nested pespectives
            featuresFromPreviousPerspective.filter(function(featureKey) {
                return !featuresFromNewPerspective.some(function(f) {
                    return featureKey === f;
                });
            }).forEach(function(featureKey) {
                featureService.disable(featureKey);
            });

            //activating any feature belonging to either the perspective or one of its nested pespectives
            var permissionPromises = [];
            featuresFromNewPerspective.filter(function(feature) {
                return !featuresFromPreviousPerspective.some(function(f) {
                    return feature === f;
                });
            }).forEach(function(featureKey) {
                permissionPromises.push(this._enableFeature(featureKey));
            }.bind(this));

            $q.all(permissionPromises).then(function() {
                if (data.activePerspective.key === NONE_PERSPECTIVE) {
                    iFrameManager.hideWaitModal();
                }
                this.unregisterPerspectiveChangedEvent = crossFrameEventService.publish(EVENT_PERSPECTIVE_CHANGED, data.activePerspective.key !== NONE_PERSPECTIVE);
            }.bind(this), function(e) {
                $log.error(e);
            });
        };

        PerspectiveService.prototype._retrievePerspective = function(key) {
            // Validation
            // Change the perspective only if it makes sense.
            if (data.activePerspective && data.activePerspective.key === key) {
                return null;
            }

            var newPerspective = this._findByKey(key);
            if (!newPerspective) {
                throw new Error("switchTo() - Couldn't find perspective with key " + key);
            }

            return newPerspective;
        };

        PerspectiveService.prototype._changeActivePerspective = function(newPerspectiveKey) {
            var newPerspective = this._retrievePerspective(newPerspectiveKey);
            if (newPerspective) {
                data.previousPerspective = data.activePerspective;
                data.activePerspective = newPerspective;
                storageService.putValueInCookie(PERSPECTIVE_COOKIE_NAME, newPerspective.key, true);
            }
            return newPerspective;
        };

        PerspectiveService.prototype.selectDefault = function() {
            return storageService.getValueFromCookie(PERSPECTIVE_COOKIE_NAME, true).then(function(cookieValue) {
                var defaultPerspective = (cookieValue && this._findByKey(cookieValue)) ? cookieValue : NONE_PERSPECTIVE;
                var perspective = (data.previousPerspective) ? data.previousPerspective.key : defaultPerspective;

                if (defaultPerspective !== NONE_PERSPECTIVE) {
                    this._disableAllFeaturesForPerspective(defaultPerspective);
                }
                this.switchTo(perspective);
            }.bind(this));
        };

        PerspectiveService.prototype.refreshPerspective = function() {
            var activePerspective = this.getActivePerspective();
            if (!activePerspective) {
                this.selectDefault();
            } else {
                this.getPerspectives().then(function(result) {
                    perspectives = result;
                    if (!this._findByKey(activePerspective.key)) {
                        this.switchTo(NONE_PERSPECTIVE);
                    } else {
                        var features = [];
                        var permissionPromises = [];

                        this._fetchAllFeatures(activePerspective, features);
                        features.forEach(function(featureKey) {
                            featureService.disable(featureKey);
                            permissionPromises.push(this._enableFeature(featureKey));
                        }.bind(this));

                        $q.all(permissionPromises).then(function() {
                            iFrameManager.hideWaitModal();
                            this.unregisterPerspectiveRefreshedEvent = crossFrameEventService.publish(EVENT_PERSPECTIVE_REFRESHED, activePerspective.key !== NONE_PERSPECTIVE);
                        }.bind(this), function(e) {
                            $log.error(e);
                        });
                    }
                }.bind(this));
            }
        };

        PerspectiveService.prototype._disableAllFeaturesForPerspective = function(perspectiveName) {
            var features = [];
            this._fetchAllFeatures(this._findByKey(perspectiveName), features);
            features.forEach(function(featureKey) {
                featureService.disable(featureKey);
            }.bind(this));
        };

        /**
         * Hopefully this will be refactored at some point, this is basicaly a confusing way of change the code
         * execution path in _retrievePerspective() after deep linking in the storefront.
         * It makes it so that the features are re-enabled without being disabled, but the code is not obvious.
         */
        PerspectiveService.prototype.clearActivePerspective = function() {
            data.previousPerspective = data.activePerspective;
            delete data.activePerspective;
        };

        PerspectiveService.prototype._clearPerspectiveFeatures = function(eventType, authenticationPayload) {
            var needToClearFeatures = isBlank(authenticationPayload) || isBlank(authenticationPayload.userHasChanged) || !!authenticationPayload.userHasChanged;
            if (needToClearFeatures) {
                // De-activates all current perspective's features (Still leaves the cookie in the system).
                var perspectiveFeatures = [];
                if (data && data.activePerspective) {
                    this._fetchAllFeatures(data.activePerspective, perspectiveFeatures);
                }

                perspectiveFeatures.forEach(function(feature) {
                    featureService.disable(feature);
                });
            }
            return $q.when();
        };

        PerspectiveService.prototype._onLogoutPerspectiveCleanup = function() {
            return this._clearPerspectiveFeatures().then(function() {
                this.clearActivePerspective();
                if (this.unregisterPerspectiveChangedEvent) {
                    this.unregisterPerspectiveChangedEvent();
                }
                if (this.unregisterPerspectiveRefreshedEvent) {
                    this.unregisterPerspectiveRefreshedEvent();
                }
                return $q.when();
            }.bind(this));
        };

        return new PerspectiveService();

    });
