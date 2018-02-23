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
var customMatchers = function() {

    var PromiseMatcherHelper = {
        states: {
            RESOLVED: 'resolved',
            REJECTED: 'rejected'
        },
        getPromiseInfo: function(promise) {
            var that = this;
            var rootScope;
            angular.mock.inject(function($injector) {
                rootScope = $injector.get('$rootScope');
            });

            var promiseInfo = {};
            promise.then(function(data) {
                promiseInfo.status = that.states.RESOLVED;
                promiseInfo.data = data;
            }, function(data) {
                promiseInfo.status = that.states.REJECTED;
                promiseInfo.data = data;
            });

            rootScope.$apply(); // Trigger promise resolution
            return promiseInfo;
        },
        getMessageForPromise: function(promiseInfo, expected) {
            return function() {
                var unresolvedMessage = 'Expected promise to be ' + promiseInfo.status;
                var badDataMessage = 'Expected promise resolved data ' + jasmine.pp(promiseInfo.data) + ' to be ' + jasmine.pp(expected);
                return promiseInfo.status !== PromiseMatcherHelper.states.RESOLVED ? unresolvedMessage : badDataMessage;
            };
        }
    };

    jasmine.addMatchers({
        toEqualData: function(util, customEqualityTesters) {
            return {
                compare: function(actual, expected) {
                    var passed = angular.equals(actual, expected);

                    return {
                        pass: passed,
                        message: 'Expected ' + actual + (passed ? '' : ' not') + ' to equal ' + expected
                    };
                }
            };
        },
        toHaveClass: function(util, customEqualityTesters) {
            return {
                compare: function(element, className) {
                    var passed = element.hasClass(className);

                    return {
                        pass: passed,
                        message: 'Expected ' + element + (passed ? '' : ' not') + ' to have class ' + className
                    };
                }
            };
        },
        toExist: function(util, customEqualityTesters) {
            return {
                compare: function(element) {
                    var passed = (window.smarteditJQuery(element).length > 0);

                    return {
                        pass: passed,
                        message: 'Expected element' + (passed ? '' : ' not') + ' to exist'
                    };
                }
            };
        },
        toBeVisible: function(util, customEqualityTesters) {
            return {
                compare: function(actual) {
                    return {
                        pass: !actual.hasClass('ng-hide')
                    };
                }
            };
        },
        fail: function(util, customEqualityTesters) {
            return {
                compare: function(actual, errorMessage) {
                    return {
                        pass: false,
                        message: errorMessage
                    };
                }
            };
        },
        toHaveThatManyAlerts: function(util, customEqualityTesters) {
            return {
                compare: function(element, expected) {
                    var actual = element.find("div.alert span").length;
                    var passed = (actual === expected);

                    return {
                        pass: passed,
                        message: 'Expected ' + element + (passed ? '' : ' not') + ' to have ' + expected + ' alert(s)'
                    };
                }
            };
        },
        alertToBe: function(util, customEqualityTesters) {
            return {
                compare: function(element, expected) {
                    var actual = element.find("div.alert-danger span").text();
                    var passed = (actual === expected);

                    return {
                        pass: passed,
                        message: 'Expected alert' + (passed ? '' : ' not') + ' to be ' + expected
                    };
                }
            };
        },
        inputToBe: function(util, customEqualityTesters) {
            return {
                compare: function(element, expected) {
                    var actual = element.find("div input[type=text]").val();
                    var passed = (actual === expected);

                    return {
                        pass: passed,
                        message: 'Expected input' + (passed ? '' : ' not') + ' to be ' + expected
                    };
                }
            };
        },
        displayToBe: function(util, customEqualityTesters) {
            return {
                compare: function(element, expected) {
                    var actual = element.find('span').html();
                    var passed = (actual === expected);

                    return {
                        pass: passed,
                        message: 'Expected ' + actual + (passed ? '' : ' not') + ' to be ' + expected
                    };
                }
            };
        },
        toBeInEditMode: function(util, customEqualityTesters) {
            return {
                compare: function(element) {
                    var passed = (element.find("> div > input[type=text][data-ng-model='editor.temp']").length === 1);

                    return {
                        pass: passed,
                        message: 'Expected' + (passed ? '' : ' not') + ' to be in edit mode'
                    };
                }
            };
        },
        calendarToBeDisplayed: function(util, customEqualityTesters) {
            return {
                compare: function(element) {
                    var passed = (element.find("ul.dropdown-menu").css('display') === 'block');

                    return {
                        pass: passed,
                        message: 'Expected calendar' + (passed ? '' : ' not') + ' to be displayed'
                    };
                }
            };
        },
        toBeEmptyFunction: function(util, customEqualityTesters) {
            return {
                compare: function(actual) {
                    var passed = (typeof actual === 'function' && actual.toString().match(/\{([\s\S]*)\}/m)[1].trim() === '');

                    return {
                        pass: passed,
                        message: 'Expected ' + actual + (passed ? '' : ' not') + ' to be an empty function'
                    };
                }
            };
        },
        toBePromise: function(util, customEqualityTesters) {
            return {
                compare: function(object) {
                    var passed = !!object.then;

                    return {
                        pass: passed,
                        message: 'Expected ' + object + (passed ? '' : ' not') + ' to be promise'
                    };
                }
            };
        },
        toBeRejected: function(util, customEqualityTesters) {
            return {
                compare: function(promise) {
                    var passed = (PromiseMatcherHelper.getPromiseInfo(promise).status === PromiseMatcherHelper.states.REJECTED);

                    return {
                        pass: passed,
                        message: 'Expected promise' + (passed ? '' : ' not') + ' to be rejected'
                    };
                }
            };
        },
        toBeResolved: function(util, customEqualityTesters) {
            return {
                compare: function(promise) {
                    var passed = (PromiseMatcherHelper.getPromiseInfo(promise).status === PromiseMatcherHelper.states.RESOLVED);

                    return {
                        pass: passed,
                        message: 'Expected promise' + (passed ? '' : ' not') + ' to be resolved'
                    };
                }
            };
        },
        toBeRejectedWithData: function(util, customEqualityTesters) {
            return {
                compare: function(promise, expected) {
                    var promiseInfo = PromiseMatcherHelper.getPromiseInfo(promise);
                    var errorMessage = PromiseMatcherHelper.getMessageForPromise(promiseInfo, expected);
                    var passed = (promiseInfo.status === PromiseMatcherHelper.states.REJECTED && angular.equals(promiseInfo.data, expected));

                    return {
                        pass: passed,
                        message: errorMessage
                    };
                }
            };
        },
        toBeResolvedWithData: function(util, customEqualityTesters) {
            return {
                compare: function(promise, expected) {
                    var promiseInfo = PromiseMatcherHelper.getPromiseInfo(promise);
                    var errorMessage = PromiseMatcherHelper.getMessageForPromise(promiseInfo, expected);
                    var passed = (promiseInfo.status === PromiseMatcherHelper.states.RESOLVED && angular.equals(promiseInfo.data, expected));

                    return {
                        pass: passed,
                        message: errorMessage
                    };
                }
            };
        },
        toBeRejectedWithDataContaining: function(util, customEqualityTesters) {
            return {
                compare: function(promise, expected) {
                    var promiseInfo = PromiseMatcherHelper.getPromiseInfo(promise);
                    var passed = (promiseInfo.status === PromiseMatcherHelper.states.REJECTED && promiseInfo.data.some(function(actual) {
                        return angular.equals(actual, expected);
                    }));

                    return {
                        pass: passed,
                        message: 'Expected promise' + (passed ? '' : ' not') + ' to be rejected with data containing ' + expected
                    };
                }
            };
        },
        toBeResolvedWithDataContaining: function(util, customEqualityTesters) {
            return {
                compare: function(promise, expected) {
                    var promiseInfo = PromiseMatcherHelper.getPromiseInfo(promise);
                    var passed = (promiseInfo.status === PromiseMatcherHelper.states.RESOLVED && promiseInfo.data.some(function(actual) {
                        return angular.equals(actual, expected);
                    }));

                    return {
                        pass: passed,
                        message: 'Expected promise' + (passed ? '' : ' not') + ' to be resolved with data containing ' + expected
                    };
                }
            };
        }
    });
};
