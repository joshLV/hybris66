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
/**
 * @ngdoc service
 * @name functionsModule
 *
 * @description
 * provides a list of useful functions that can be used as part of the SmartEdit framework.
 */
angular.module('functionsModule', ['yjqueryModule', 'yLoDashModule', 'ngSanitize'])

    .factory('ParseError', function() {
        var ParseError = function(value) {
            this.value = value;
        };
        return ParseError;
    })
    /**
     * @ngdoc service
     * @name functionsModule.getOrigin
     *
     * @description
     * returns document location origin
     * Some browsers still do not support W3C document.location.origin, this function caters for gap.
     */
    .factory('getOrigin', function() {
        return function() {
            return window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
        };
    })
    /**
     * @ngdoc service
     * @name functionsModule.isBlank
     *
     * @description
     * <b>isBlank</b> will check if a given string is undefined or null or empty.
     * - returns TRUE for undefined / null/ empty string
     * - returns FALSE otherwise
     *
     * @param {String} inputString any input string.
     *
     * @returns {boolean} true if the string is null else false
     */
    .factory('isBlank', function() {
        return function(value) {
            return (typeof value === 'undefined' || value === null || value === "null" || value.toString().trim().length === 0);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.extend
     *
     * @description
     * <b>extend</b> provides a convenience to either default a new child or "extend" an existing child with the prototype of the parent
     *
     * @param {Class} ParentClass which has a prototype you wish to extend.
     * @param {Class} ChildClass will have its prototype set.
     *
     * @returns {Class} ChildClass which has been extended
     */
    .factory('extend', function() {
        return function(ParentClass, ChildClass) {
            if (!ChildClass) {
                ChildClass = function() {};
            }
            ChildClass.prototype = Object.create(ParentClass.prototype);
            return ChildClass;
        };
    })

    /**
     * @deprecated since 6.6, use {@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind bind} instead
     * @ngdoc service
     * @name functionsModule.hitch
     *
     * @description
     * <b>hitch</b> will create a new function that will pass our desired context (scope) to the given function.
     * This method will also pre-bind the given parameters.
     *
     * @param {Object} scope scope that is to be assigned.
     * @param {Function} method the method that needs binding.
     *
     * @returns {Function} a new function thats binded to the given scope
     */
    .factory('hitch', function() {
        return function(scope, method) {

            var argumentArray = Array.prototype.slice.call(arguments); // arguments is not an array
            // (from  http://www.sitepoint.com/arguments-a-javascript-oddity/)

            var preboundArguments = argumentArray.slice(2);

            return function lockedMethod() {

                // from here, "arguments" are the arguments passed to lockedMethod

                var postBoundArguments = Array.prototype.slice.call(arguments);

                return method.apply(scope, preboundArguments.concat(postBoundArguments));

            };

        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.customTimeout
     *
     * @description
     * <b>customTimeout</b> will call the javascrit's native setTimeout method to execute a given function after a specified period of time.
     * This method is better than using $timeout since it is difficult to assert on $timeout during end-to-end testing.
     *
     * @param {Function} func function that needs to be executed after the specified duration.
     * @param {Number} duration time in milliseconds.
     */
    .factory('customTimeout', function($rootScope) {
        return function(func, duration) {
            setTimeout(function() {
                func();
                $rootScope.$digest();
            }, duration);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.copy
     *
     * @description
     * <b>copy</b> will do a deep copy of the given input object.
     *
     * @param {Object} candidate the javaScript value that needs to be deep copied.
     *
     * @returns {Object} A deep copy of the input
     */
    .factory('copy', function() {
        return function(candidate) {
            return JSON.parse(JSON.stringify(candidate));
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.merge
     *
     * @description
     * <b>merge</b> will merge the contents of two objects together into the first object.
     *
     * @param {Object} target any JavaScript object.
     * @param {Object} source any JavaScript object.
     *
     * @returns {Object} a new object as a result of merge
     */
    .factory('merge', function(yjQuery) {
        return function(source, target) {

            yjQuery.extend(source, target);

            return source;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.getQueryString
     *
     * @description
     * <b>getQueryString</b> will convert a given object into a query string.
     *
     * Below is the code snippet for sample input and sample output:
     *
     * <pre>
     * var params = {
     *  key1 : 'value1',
     *  key2 : 'value2',
     *  key3 : 'value3'
     *  }
     *
     *  var output = getQueryString(params);
     *
     *  // The output is '?&key1=value1&key2=value2&key3=value3'
     *
     *</pre>
     *
     * @param {Object} params Object containing a list of params.
     *
     * @returns {String} a query string
     */
    .factory('getQueryString', function() {
        return function(params) {

            var queryString = "";
            if (params) {
                for (var param in params) {
                    queryString += '&' + param + "=" + params[param];
                }
            }
            return "?" + queryString;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.getURI
     *
     * @description
     * Will return the URI part of a URL
     * @param {String} url the URL the URI of which is to be returned
     */
    .factory('getURI', function() {
        return function(url) {
            return url && url.indexOf("?") > -1 ? url.split("?")[0] : url;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.parseQuery
     *
     * @description
     * <b>parseQuery</b> will convert a given query string to an object.
     *
     * Below is the code snippet for sample input and sample output:
     *
     * <pre>
     * var query = '?key1=value1&key2=value2&key3=value3';
     *
     * var output = parseQuery(query);
     *
     * // The output is { key1 : 'value1', key2 : 'value2', key3 : 'value3' }
     *
     *</pre>
     *
     * @param {String} query String that needs to be parsed.
     *
     * @returns {Object} an object containing all params of the given query
     */
    .factory('parseQuery', function() {
        return function(str) {

            var objURL = {};

            str.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
                objURL[$1] = $3;
            });
            return objURL;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.trim
     *
     * @description
     * <b>trim</b> will remove spaces at the beginning and end of a given string.
     *
     * @param {String} inputString any input string.
     *
     * @returns {String} the newly modified string without spaces at the beginning and the end
     */
    .factory('trim', function() {

        return function(aString) {
            var regExpBeginning = /^\s+/;
            var regExpEnd = /\s+$/;
            return aString.replace(regExpBeginning, "").replace(regExpEnd, "");
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.convertToArray
     *
     * @description
     * <b>convertToArray</b> will convert the given object to array.
     * The output array elements are an object that has a key and value,
     * where key is the original key and value is the original object.
     *
     * @param {Object} inputObject any input object.
     *
     * @returns {Array} the array created from the input object
     */
    .factory('convertToArray', function() {

        return function(object) {
            var configuration = [];
            for (var key in object) {
                if (key.indexOf('$') !== 0 && key.indexOf('toJSON') !== 0) {
                    configuration.push({
                        key: key,
                        value: object[key]
                    });
                }
            }
            return configuration;
        };

    })

    /**
     * @ngdoc service
     * @name functionsModule.injectJS
     *
     * @description
     * <b>injectJS</b> will inject script tags into html for a given set of sources.
     *
     */
    .factory('injectJS', function() {

        function getInjector() {
            return $script;
        }

        return {
            getInjector: getInjector,

            /**
             * @ngdoc method
             * @name functionsModule.injectJS#execute
             * @methodOf functionsModule.injectJS
             *
             * @description
             * <b>execute</b> will extract a given set of sources from the provided configuration object
             * and then inject each source as a JavaScript source tag and potential callbacks once all the
             * sources are wired.
             *
             * @param {Object} configuration - a given set of configurations.
             * @param {Array} configuration.sources - an array of sources that needs to be added.
             * @param {Function} configuration.callback - Callback to be triggered once all the sources are wired.
             */
            execute: function(conf) {
                var srcs = conf.srcs;
                var index = conf.index;
                var callback = conf.callback;
                if (index === undefined) {
                    index = 0;
                }
                if (srcs[index] !== undefined) {
                    this.getInjector()(srcs[index], function() {
                        if (index + 1 < srcs.length) {
                            this.execute({
                                srcs: srcs,
                                index: index + 1,
                                callback: callback
                            });
                        } else if (typeof callback === 'function') {
                            callback();
                        }
                    }.bind(this));

                }
            }
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.uniqueArray
     *
     * @description
     * <b>uniqueArray</b> will return the first Array argument supplemented with new entries from the second Array argument.
     *
     * @param {Array} array1 any JavaScript array.
     * @param {Array} array2 any JavaScript array.
     */
    .factory('uniqueArray', function() {

        return function(array1, array2) {

            array2.forEach(function(instance) {
                if (array1.indexOf(instance) === -1) {
                    array1.push(instance);
                }
            });

            return array1;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.regExpFactory
     *
     * @description
     * <b>regExpFactory</b> will convert a given pattern into a regular expression.
     * This method will prepend and append a string with ^ and $ respectively replaces
     * and wildcards (*) by proper regex wildcards.
     *
     * @param {String} pattern any string that needs to be converted to a regular expression.
     *
     * @returns {RegExp} a regular expression generated from the given string.
     *
     */
    .factory('regExpFactory', function() {

        return function(pattern) {

            var onlyAlphanumericsRegex = new RegExp(/^[a-zA-Z\d]+$/i);
            var antRegex = new RegExp(/^[a-zA-Z\d\*]+$/i);

            var regexpKey;
            if (onlyAlphanumericsRegex.test(pattern)) {
                regexpKey = ['^', '$'].join(pattern);
            } else if (antRegex.test(pattern)) {
                regexpKey = ['^', '$'].join(pattern.replace(/\*/, '.*'));
            } else {
                regexpKey = pattern;
            }

            return new RegExp(regexpKey, 'g');
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.generateIdentifier
     *
     * @description
     * <b>generateIdentifier</b> will generate a unique string based on system time and a random generator.
     *
     * @returns {String} a unique identifier.
     *
     */
    .factory('generateIdentifier', function() {
        return function() {
            var d = new Date().getTime();
            if (window.performance && typeof window.performance.now === "function") {
                d += window.performance.now(); //use high-precision timer if available
            }
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
            });
            return uuid;
        };
    })




    /**
     * @ngdoc service
     * @name functionsModule.escapeHtml
     *
     * @description
     * <b>escapeHtml</b> will escape &, <, >, " and ' characters .
     *
     * @param {String} a string that needs to be escaped.
     *
     * @returns {String} the escaped string.
     *
     */
    .factory('escapeHtml', function() {
        return function(str) {
            if (typeof str === 'string') {
                return str.replace(/&/g, '&amp;')
                    .replace(/>/g, '&gt;')
                    .replace(/</g, '&lt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&apos;');
            } else {
                return str;
            }
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.sanitize
     *
     * @description
     * <b>escapes any harmful scripting from a string, leaves innocuous HTML untouched/b>
     *
     * @param {String} a string that needs to be sanitized.
     *
     * @returns {String} the sanitized string.
     *
     */
    .factory('sanitize', function(isBlank) {
        return function(str) {
            return !isBlank(str) ? str.replace(/(?=[()])/g, '\\') : str;
            //return $sanitize(string);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.sanitizeHTML
     *
     * @description
     * <b>sanitizeHTML</b> will remove breaks and space .
     *
     * @param {String} a string that needs to be escaped.
     *
     * @returns {String} the sanitized HTML.
     *
     */
    .factory('sanitizeHTML', function(isBlank) {
        return function(obj) {
            var result = angular.copy(obj);
            if (!isBlank(result)) {
                result = result.replace(/(\r\n|\n|\r)/gm, '').replace(/>\s+</g, '><').replace(/<\/br\>/g, '');
            }
            return result;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.toPromise
     *
     * @description
     * <b>toPromise</> transforms a function into a function that is guaranteed to return a Promise that resolves to the
     * original return value of the function, rejects with the rejected return value and rejects with an exceptino object when the invocation fails
     */
    .factory('toPromise', function($q, $log) {
        return function(method, context) {
            return function() {
                try {
                    return $q.when(method.apply(context, arguments));
                } catch (e) {
                    $log.error('execution of a method that was turned into a promise failed');
                    $log.error(e);
                    return $q.reject(e);
                }
            };
        };
    })


    /**
     * Checks if `value` is a function.
     *
     * @static
     * @category Objects
     * @param {*} value The value to check.
     * @returns {boolean} Returns `true` if the `value` is a function, else `false`.
     */
    .factory('isFunction', function() {
        return function(value) {
            return typeof value === 'function';
        };
    })

    // check if the value is the ECMAScript language type of Object
    .factory('isObject', function() {
        /** Used to determine if values are of the language type Object */
        var objectTypes = {
            'boolean': false,
            'function': true,
            'object': true,
            'number': false,
            'string': false,
            'undefined': false
        };
        return function(value) {
            return !!(value && objectTypes[typeof value]);
        };
    })

    /**
     * Creates a function that will delay the execution of `func` until after
     * `wait` milliseconds have elapsed since the last time it was invoked.
     * Provide an options object to indicate that `func` should be invoked on
     * the leading and/or trailing edge of the `wait` timeout. Subsequent calls
     * to the debounced function will return the result of the last `func` call.
     *
     * Note: If `leading` and `trailing` options are `true` `func` will be called
     * on the trailing edge of the timeout only if the the debounced function is
     * invoked more than once during the `wait` timeout.
     *
     * @static
     * @category Functions
     * @param {Function} func The function to debounce.
     * @param {number} wait The number of milliseconds to delay.
     * @param {Object} [options] The options object.
     * @param {boolean} [options.leading=false] Specify execution on the leading edge of the timeout.
     * @param {number} [options.maxWait] The maximum time `func` is allowed to be delayed before it's called.
     * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
     * @returns {Function} Returns the new debounced function.
     * @example
     *
     * // avoid costly calculations while the window size is in flux
     * var lazyLayout = lodash.debounce(calculateLayout, 150);
     * yjQuery(window).on('resize', lazyLayout);
     *
     * // execute `sendMail` when the click event is fired, debouncing subsequent calls
     * yjQuery('#postbox').on('click', lodash.debounce(sendMail, 300, {
     *   'leading': true,
     *   'trailing': false
     * });
     *
     * // ensure `batchLog` is executed once after 1 second of debounced calls
     * var source = new EventSource('/stream');
     * source.addEventListener('message', lodash.debounce(batchLog, 250, {
     *   'maxWait': 1000
     * }, false);
     */
    .factory('debounce', function(isFunction, isObject) {
        function TypeError() {

        }

        return function(func, wait, options) {
            var args;
            var maxTimeoutId;
            var result;
            var stamp;
            var thisArg;
            var timeoutId;
            var trailingCall;
            var leading;
            var lastCalled = 0;
            var maxWait = false;
            var trailing = true;
            var isCalled;

            if (!isFunction(func)) {
                throw new TypeError();
            }
            wait = Math.max(0, wait) || 0;
            if (options === true) {
                leading = true;
                trailing = false;
            } else if (isObject(options)) {
                leading = options.leading;
                maxWait = 'maxWait' in options && (Math.max(wait, options.maxWait) || 0);
                trailing = 'trailing' in options ? options.trailing : trailing;
            }
            var delayed = function() {
                var remaining = wait - (Date.now() - stamp);
                if (remaining <= 0) {
                    if (maxTimeoutId) {
                        clearTimeout(maxTimeoutId);
                    }
                    isCalled = trailingCall;
                    maxTimeoutId = timeoutId = trailingCall = undefined;
                    if (isCalled) {
                        lastCalled = Date.now();
                        result = func.apply(thisArg, args);
                        if (!timeoutId && !maxTimeoutId) {
                            args = thisArg = null;
                        }
                    }
                } else {
                    timeoutId = setTimeout(delayed, remaining);
                }
            };

            var maxDelayed = function() {
                if (timeoutId) {
                    clearTimeout(timeoutId);
                }
                maxTimeoutId = timeoutId = trailingCall = undefined;
                if (trailing || (maxWait !== wait)) {
                    lastCalled = Date.now();
                    result = func.apply(thisArg, args);
                    if (!timeoutId && !maxTimeoutId) {
                        args = thisArg = null;
                    }
                }
            };

            return function() {
                args = arguments;
                stamp = Date.now();
                thisArg = this;
                trailingCall = trailing && (timeoutId || !leading);
                var leadingCall;
                var isCalled;

                if (maxWait === false) {
                    leadingCall = leading && !timeoutId;
                } else {
                    if (!maxTimeoutId && !leading) {
                        lastCalled = stamp;
                    }
                    var remaining = maxWait - (stamp - lastCalled);
                    isCalled = remaining <= 0;

                    if (isCalled) {
                        if (maxTimeoutId) {
                            maxTimeoutId = clearTimeout(maxTimeoutId);
                        }
                        lastCalled = stamp;
                        result = func.apply(thisArg, args);
                    } else if (!maxTimeoutId) {
                        maxTimeoutId = setTimeout(maxDelayed, remaining);
                    }
                }
                if (isCalled && timeoutId) {
                    timeoutId = clearTimeout(timeoutId);
                } else if (!timeoutId && wait !== maxWait) {
                    timeoutId = setTimeout(delayed, wait);
                }
                if (leadingCall) {
                    isCalled = true;
                    result = func.apply(thisArg, args);
                }
                if (isCalled && !timeoutId && !maxTimeoutId) {
                    args = thisArg = null;
                }
                return result;
            };
        };
    })

    /**
     * Creates a function that, when executed, will only call the `func` function
     * at most once per every `wait` milliseconds. Provide an options object to
     * indicate that `func` should be invoked on the leading and/or trailing edge
     * of the `wait` timeout. Subsequent calls to the throttled function will
     * return the result of the last `func` call.
     *
     * Note: If `leading` and `trailing` options are `true` `func` will be called
     * on the trailing edge of the timeout only if the the throttled function is
     * invoked more than once during the `wait` timeout.
     *
     * @static
     * @category Functions
     * @param {Function} func The function to throttle.
     * @param {number} wait The number of milliseconds to throttle executions to.
     * @param {Object} [options] The options object.
     * @param {boolean} [options.leading=true] Specify execution on the leading edge of the timeout.
     * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
     * @returns {Function} Returns the new throttled function.
     * @example
     *
     * // avoid excessively updating the position while scrolling
     * var throttled = lodash.throttle(updatePosition, 100);
     * yjQuery(window).on('scroll', throttled);
     *
     * // execute `renewToken` when the click event is fired, but not more than once every 5 minutes
     * yjQuery('.interactive').on('click', lodash.throttle(renewToken, 300000, {
     *   'trailing': false
     * }));
     */
    .factory('throttle', function(debounce, isFunction, isObject) {
        return function(func, wait, options) {
            var leading = true;
            var trailing = true;

            if (!isFunction(func)) {
                throw new TypeError();
            }
            if (options === false) {
                leading = false;
            } else if (isObject(options)) {
                leading = 'leading' in options ? options.leading : leading;
                trailing = 'trailing' in options ? options.trailing : trailing;
            }
            options = {};
            options.leading = leading;
            options.maxWait = wait;
            options.trailing = trailing;

            return debounce(func, wait, options);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.parseHTML
     *
     * @description
     * parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.
     *
     * @param {String} stringHTML, the string representation of the HTML to parse
     */
    .factory('parseHTML', function(yjQuery) {
        return function(stringHTML) {
            return yjQuery.parseHTML(stringHTML);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.unsafeParseHTML
     *
     * @description
     * parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.
     * Note - as this preserves the JavaScript present it must only be used on HTML strings originating
     * from a known safe location. Failure to do so may result in an XSS vulnerability.
     *
     * @param {String} stringHTML, the string representation of the HTML to parse
     */
    .factory('unsafeParseHTML', function(yjQuery) {
        return function(stringHTML) {
            return yjQuery.parseHTML(stringHTML, null, true);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.extractFromElement
     *
     * @description
     * parses a string HTML into a queriable DOM object
     *
     * @param {Object} parent, the DOM element from which we want to extract matching selectors
     * @param {String} extractionSelector, the yjQuery selector identifying the elements to be extracted
     */
    .factory('extractFromElement', function(yjQuery) {
        return function(parent, extractionSelector) {
            parent = yjQuery(parent);
            return parent.filter(extractionSelector).add(parent.find(extractionSelector));
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.closeOpenModalsOnBrowserBack
     *
     * @description
     * close any open modal window when a user clicks browser back button
     *
     * @param {Object} modalStack, the $modalStack service of angular-ui.
     */
    .factory('closeOpenModalsOnBrowserBack', function($uibModalStack) {
        return function() {
            if ($uibModalStack.getTop()) {
                $uibModalStack.dismissAll();
            }
        };
    })
    /**
     * @ngdoc service
     * @name functionsModule.service:URIBuilder
     *
     * @description
     * builder or URIs, build() method must be invoked to actually retrieve a URI
     *
     * @param {Object} modalStack, the $modalStack service of angular-ui.
     */
    .factory('URIBuilder', function(lodash) {

        function URIBuilder(uri) {

            this.uri = uri;

            this.build = function() {
                return this.uri;
            };

            /**
             * @ngdoc method
             * @name functionsModule.service:URIBuilder#replaceParams
             * @methodOf functionsModule.service:URIBuilder
             *
             * @description
             * Substitute all placeholders in the URI with the matching values in the given params
             *
             * @param {Object} params a map of placeholder names / values
             */
            this.replaceParams = function(params) {
                var clone = lodash.cloneDeep(this);
                if (params) {
                    //order the keys by descending length
                    var keys = Object.keys(params).sort(function(a, b) {
                        return b.length - a.length;
                    });
                    keys.forEach(function(key) {
                        var re = new RegExp('\\b' + key + '\\b');
                        clone.uri = clone.uri.replace(':' + key, params[key]).replace(re, params[key]);
                    });
                }
                return clone;
            };
        }

        return URIBuilder;
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:getDataFromResponse
     *
     * @description
     * when provided with a response returned from a backend call, will filter the response
     * to retrieve the data of interest.
     *
     * @param {Object} response, response returned from a backend call.
     * @returns {Array} Returns the array from the response.
     */
    .factory('getDataFromResponse', function() {
        return function(response) {
            var dataKey = Object.keys(response).filter(function(key) {
                return response[key] instanceof Array;
            })[0];

            return response[dataKey];
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:getKeyHoldingDataFromResponse
     *
     * @description
     * when provided with a response returned from a backend call, will filter the response
     * to retrieve the key holding the data of interest.
     *
     * @param {Object} response, response returned from a backend call.
     * @returns {String} Returns the name of the key holding the array from the response.
     */
    .factory('getKeyHoldingDataFromResponse', function() {
        return function(response) {
            var dataKey = Object.keys(response).filter(function(key) {
                return response[key] instanceof Array;
            })[0];

            return dataKey;
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:resetObject
     *
     * @description
     * Resets a given object's properties' values
     *
     * @param {Object} targetObject, the object to reset
     * @param {Object} modelObject, an object that contains the structure that targetObject should have after a reset

     * @returns {Object} Returns the object that has been reset
     */
    .factory('resetObject', function(copy) {
        return function(targetObject, modelObject) {
            if (!targetObject) {
                targetObject = copy(modelObject);
            } else {
                for (var i in targetObject) {
                    delete targetObject[i];
                }
                angular.extend(targetObject, copy(modelObject));
            }

            return targetObject;
        };
    })
    /**
     * @ngdoc service
     * @name functionsModule.service:isFunctionEmpty
     *
     * @description
     * Will determine whether a function body is empty or should be considered empty for proxying purposes
     *
     * @param {Function} fn, the function to evaluate

     * @returns {Boolean} a boolean.
     */
    .factory('isFunctionEmpty', function() {
        return function(fn) {
            return fn.toString().match(/\{([\s\S]*)\}/m)[1].trim() === '' || /proxyFunction/g.test(fn.toString().replace(/\s/g, ""));
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:isObjectEmptyDeep
     *
     * @description
     * Will check if the object is empty and will return true if each and every property of the object is empty
     *
     * @param {Object} value, the value to evaluate

     * @returns {Boolean} a boolean.
     */
    .factory('isObjectEmptyDeep', function(lodash) {
        return function(value) {
            if (lodash.isObject(value)) {
                for (var key in value) {
                    if (!lodash.isEmpty(value[key])) {
                        return false;
                    }
                }
                return true;
            }
            return lodash.isEmpty(value);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:isAllTruthy
     *
     * @description
     * Iterate on the given array of Functions, return true if each function returns true
     *
     * @param {Array} arguments the functions
     *
     * @return {Boolean} true if every function returns true
     */
    .factory('isAllTruthy', function() {
        return function() {
            var fns = Array.prototype.slice.call(arguments);
            return function() {
                var args = arguments;
                return fns.every(function(f) {
                    return f.apply(f, args);
                });
            };
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:isAnyTruthy
     *
     * @description
     * Iterate on the given array of Functions, return true if at least one function returns true
     *
     * @param {Array} arguments the functions
     *
     * @return {Boolean} true if at least one function returns true
     */
    .factory('isAnyTruthy', function() {
        return function() {
            var fns = Array.prototype.slice.call(arguments);
            return function() {
                var args = arguments;
                return fns.some(function(f) {
                    return f.apply(f, args);
                });
            };
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:formatDateAsUtc
     *
     * @description
     * Formats provided dateTime as utc.
     *
     * @param {Object|String} dateTime DateTime to format in utc.
     *
     * @return {String} formatted string.
     */
    .factory('formatDateAsUtc', function(DATE_CONSTANTS) {
        return function(dateTime) {
            return moment(dateTime).utc().format(DATE_CONSTANTS.MOMENT_ISO);
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.getEncodedString
     *
     * @description
     * <b>getEncodedString</b> will creates a base-64 encoded ASCII string
     * from the String passed as input
     *
     * @returns {String} a base-64 encoded ASCII string.
     *
     */
    .factory('getEncodedString', function() {
        return function(passedString) {
            if (typeof passedString === "string") {
                return btoa(passedString);
            } else {
                throw new Error('getEncodedString called with input of type "' + typeof passedString + '" when only "string" is accepted.');
            }

        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.encode
     *
     * @description
     * will return a encoded value for any JSON object passed as argument
     * @param {object} JSON object to be encoded
     */
    .factory('encode', function() {
        return function(object) {
            /* first we use encodeURIComponent to get percent-encoded UTF-8,
             * then we convert the percent encodings into raw bytes which
             * can be fed into btoa.
             * from https://developer.mozilla.org/en-US/docs/Web/API/WindowBase64/Base64_encoding_and_decoding
             */
            return btoa(encodeURIComponent(JSON.stringify(object)).replace(/%([0-9A-F]{2})/g,
                function toSolidBytes(match, p1) {
                    return String.fromCharCode('0x' + p1);
                }));
        };
    })

    /**
     * @ngdoc service
     * @name functionsModule.service:compareHTMLElementsPosition
     *
     * @description
     * A function to sort an array containing DOM elements according to their position in the DOM
     *
     * @param {key =} key Optional key value to get the
     *
     * @return {Function} the compare function to use with array.sort(compareFunction) to order DOM elements as they would appear in the DOM
     */
    .factory('compareHTMLElementsPosition', function() {
        return function(key) {
            return function(a, b) {
                if (key) {
                    a = a[key];
                    b = b[key];
                }
                if (a === b) {
                    return 0;
                }
                if (!a.compareDocumentPosition) {
                    // support for IE8 and below
                    return a.sourceIndex - b.sourceIndex;
                }
                if (a.compareDocumentPosition(b) & 2) {
                    // b comes before a
                    return 1;
                }
                return -1;
            };
        };
    });

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
/**
 * @ngdoc overview
 * @name yjqueryModule
 * @description
 * This module manages the use of the jQuery library in SmartEdit.
 * It enables smartEdit to work with a "noConflict" version of jQuery in a storefront that may contain another version
 */
angular.module('yjqueryModule', [])
    /**
     * As a configuration step for this module, add the getCssPath method to jquery selectors. This method will return
     * the CSS path of the wrapped JQuery element.
     */
    .run(function(yjQuery) {
        yjQuery.fn.extend({        
            getCssPath: function() {            
                var path;
                var node = this;            
                while (node.length) {                
                    var realNode = node[0];                    
                    var name = realNode.className;                
                    if (realNode.tagName === 'BODY') {                    
                        break;                
                    }                
                    node = node.parent();                
                    path = name + (path ? '>' + path : '');            
                }            
                return path;        
            }    
        });
    })
    /**
     * @ngdoc object
     * @name yjqueryModule.yjQuery
     * @description
     * 
     * Expose a jQuery wrapping factory all the while preserving potentially pre-existing jQuery in storefront and smartEditContainer
     */
    /* forbiddenNameSpaces:false */
    .factory('yjQuery', function() {

        var namespace = "smarteditJQuery";

        if (!window[namespace]) {
            if (window.$ && window.$.noConflict) {
                window[namespace] = window.$.noConflict();
            } else {
                window[namespace] = window.$;
            }
        }
        return window[namespace];
    });

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
(function() {

    var STORE_FRONT_CONTEXT_VAR = '/storefront';

    var CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    var CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
    var CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

    var PAGE_CONTEXT_CATALOG = 'CURRENT_PAGE_CONTEXT_CATALOG';
    var PAGE_CONTEXT_CATALOG_VERSION = 'CURRENT_PAGE_CONTEXT_CATALOG_VERSION';
    var PAGE_CONTEXT_SITE_ID = 'CURRENT_PAGE_CONTEXT_SITE_ID';

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:UriContext
     *
     * @description
     * A map that contains the necessary site and catalog information for CMS services and directives.
     * It contains the following keys:
     * {@link resourceLocationsModule.object:CONTEXT_SITE_ID CONTEXT_SITE_ID} for the site uid,
     * {@link resourceLocationsModule.object:CONTEXT_CATALOG CONTEXT_CATALOG} for the catalog uid,
     * {@link resourceLocationsModule.object:CONTEXT_CATALOG_VERSION CONTEXT_CATALOG_VERSION} for the catalog version.
     */

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PageUriContext
     *
     * @description
     * A map that contains the necessary site and catalog information for CMS services and directives for a given page.
     * It contains the following keys:
     * {@link resourceLocationsModule.object:PAGE_CONTEXT_SITE_ID PAGE_CONTEXT_SITE_ID} for the site uid,
     * {@link resourceLocationsModule.object:PAGE_CONTEXT_CATALOG PAGE_CONTEXT_CATALOG} for the catalog uid,
     * {@link resourceLocationsModule.object:PAGE_CONTEXT_CATALOG_VERSION PAGE_CONTEXT_CATALOG_VERSION} for the catalog version.
     */

    angular.module('resourceLocationsModule', [])

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONTEXT_SITE_ID
         *
         * @description
         * Constant containing the name of the site uid placeholder in URLs
         */
        .constant('CONTEXT_SITE_ID', CONTEXT_SITE_ID)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONTEXT_CATALOG
         *
         * @description
         * Constant containing the name of the catalog uid placeholder in URLs
         */
        .constant('CONTEXT_CATALOG', CONTEXT_CATALOG)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONTEXT_CATALOG_VERSION
         *
         * @description
         * Constant containing the name of the catalog version placeholder in URLs
         */
        .constant('CONTEXT_CATALOG_VERSION', CONTEXT_CATALOG_VERSION)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PAGE_CONTEXT_SITE_ID
         *
         * @description
         * Constant containing the name of the current page site uid placeholder in URLs
         */
        .constant('PAGE_CONTEXT_SITE_ID', PAGE_CONTEXT_SITE_ID)

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PAGE_CONTEXT_CATALOG
         *
         * @description
         * Constant containing the name of the current page catalog uid placeholder in URLs
         */
        .constant('PAGE_CONTEXT_CATALOG', PAGE_CONTEXT_CATALOG)

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PAGE_CONTEXT_CATALOG_VERSION
         *
         * @description
         * Constant containing the name of the current page catalog version placeholder in URLs
         */
        .constant('PAGE_CONTEXT_CATALOG_VERSION', PAGE_CONTEXT_CATALOG_VERSION)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SMARTEDIT_ROOT
         *
         * @description
         * the name of the webapp root context
         */
        .constant('SMARTEDIT_ROOT', 'smartedit')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP
         *
         * @description
         * to calculate platform domain URI, this regular expression will be used
         */
        .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smartedit/)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONFIGURATION_URI
         *
         * @description
         * the name of the SmartEdit configuration API root
         */
        .constant('CONFIGURATION_URI', '/smartedit/configuration/:key')

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONFIGURATION_COLLECTION_URI
         *
         * @description
         * The SmartEdit configuration collection API root
         */
        .constant('CONFIGURATION_COLLECTION_URI', '/smartedit/configuration')

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CMSWEBSERVICES_RESOURCE_URI
         *
         * @description
         * Constant for the cmswebservices API root
         */
        .constant('CMSWEBSERVICES_RESOURCE_URI', '/cmswebservices')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT
         *
         * @description
         * When configuration is not available yet to provide authenticationMap, one needs a default authentication entry point to access configuration API itself
         */
        .constant('DEFAULT_AUTHENTICATION_ENTRY_POINT', '/authorizationserver/oauth/token')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_CLIENT_ID
         *
         * @description
         * The default OAuth 2 client id to use during authentication.
         */
        .constant('DEFAULT_AUTHENTICATION_CLIENT_ID', 'smartedit')
        /**
         * Root resource URI of i18n API 
         */
        .constant('I18N_ROOT_RESOURCE_URI', '/smarteditwebservices/v1/i18n')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:I18N_RESOURCE_URI
         *
         * @description
         * Resource URI to fetch the i18n initialization map for a given locale.
         */
        .constant('I18N_RESOURCE_URI', '/smarteditwebservices/v1/i18n/translations')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:I18N_LANGUAGE_RESOURCE_URI
         *
         * @description
         * Resource URI to fetch the supported i18n languages.
         */
        .constant('I18N_LANGUAGES_RESOURCE_URI', '/smarteditwebservices/v1/i18n/languages')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANGUAGE_RESOURCE_URI
         *
         * @description
         * Resource URI of the languages REST service.
         */
        .constant('LANGUAGE_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/languages')
        .constant('PRODUCT_RESOURCE_API', '/cmssmarteditwebservices/v1/sites/:siteUID/products/:productUID')
        .constant('PRODUCT_LIST_RESOURCE_API', '/cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:catalogVersion/products')
        .constant('PRODUCT_CATEGORY_RESOURCE_URI', '/cmssmarteditwebservices/v1/sites/:siteUID/categories/:categoryUID')
        .constant('PRODUCT_CATEGORY_SEARCH_RESOURCE_URI', '/cmssmarteditwebservices/v1/productcatalogs/:catalogId/versions/:catalogVersion/categories')

        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SITES_RESOURCE_URI
         *
         * @description
         * Resource URI of the sites REST service.
         */
        .constant('SITES_RESOURCE_URI', '/cmswebservices/v1/sites')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANDING_PAGE_PATH
         *
         * @description
         * Path of the landing page
         */
        .constant('LANDING_PAGE_PATH', '/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STOREFRONT_PATH
         *
         * @description
         * Path of the storefront
         */
        .constant('STOREFRONT_PATH', STORE_FRONT_CONTEXT_VAR + '/:siteId/:catalogId/:catalogVersion/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STOREFRONT_PATH_WITH_PAGE_ID
         *
         * @description
         * Path of the storefront with a page ID
         */
        .constant('STOREFRONT_PATH_WITH_PAGE_ID', STORE_FRONT_CONTEXT_VAR + '/:siteId/:catalogId/:catalogVersion/:pageId')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STORE_FRONT_CONTEXT
         *
         * @description
         * to fetch the store front context for inflection points.
         */
        .constant('STORE_FRONT_CONTEXT', STORE_FRONT_CONTEXT_VAR)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CATALOGS_PATH
         *
         * @description
         * Path of the catalogs
         */
        .constant('CATALOGS_PATH', '/cmswebservices/v1/catalogs/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:MEDIA_PATH
         *
         * @description
         * Path of the media
         */
        .constant('MEDIA_PATH', '/cmswebservices/v1/media')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CMSWEBSERVICES_PATH
         *
         * @description
         * Regular expression identifying CMS related URIs
         */
        .constant('CMSWEBSERVICES_PATH', /\/cmssmarteditwebservices|\/cmswebservices/)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PREVIEW_RESOURCE_URI
         *
         * @description
         * Path of the preview ticket API
         */
        .constant('PREVIEW_RESOURCE_URI', '/previewwebservices/v1/preview')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:ENUM_RESOURCE_URI
         *
         * @description
         * Path to fetch list of values of a given enum type
         */
        .constant('ENUM_RESOURCE_URI', "/cmswebservices/v1/enums")
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PERMISSIONSWEBSERVICES_RESOURCE_URI
         *
         * @description
         * Path to fetch permissions of a given type
         */
        .constant('USER_GLOBAL_PERMISSIONS_RESOURCE_URI', "/permissionswebservices/v1/permissions/principals/:user/global")
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SYNC_PATH
         *
         * @description
         * Path of the synchronization service
         */
        .constant('SYNC_PATH', '/cmswebservices/v1/catalogs/:catalog/versions/Staged/synchronizations/versions/Online')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:MEDIA_RESOURCE_URI
         *
         * @description
         * Resource URI of the media REST service.
         */
        .constant('MEDIA_RESOURCE_URI', '/cmswebservices/v1/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/media')
        /**
         * @name resourceLocationsModule.object:TYPES_RESOURCE_URI
         *
         * @description
         * Resource URI of the component types REST service.
         */
        .constant('TYPES_RESOURCE_URI', '/cmswebservices/v1/types')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STRUCTURES_RESOURCE_URI
         *
         * @description
         * Resource URI of the component structures REST service.
         */
        .constant('STRUCTURES_RESOURCE_URI', '/cmssmarteditwebservices/v1/structures')

        /**
         * @ngdoc service
         * @name resourceLocationsModule.resourceLocationToRegex
         *
         * @description
         * Generates a regular expresssion matcher from a given resource location URL, replacing predefined keys by wildcard
         * matchers.
         *
         * Example:
         * <pre>
         *     // Get a regex matcher for the someResource endpoint, ie: /\/smarteditwebservices\/someResource\/.*$/g
         *     var endpointRegex = resourceLocationToRegex('/smarteditwebservices/someResource/:id');
         *
         *     // Use the regex to match hits to the mocked HTTP backend. This regex will match for any ID passed in to the
         *     // someResource endpoint.
         *     $httpBackend.whenGET(endpointRegex).respond({someKey: 'someValue'});
         * </pre>
         */
        .factory('resourceLocationToRegex', function() {
            return function(str) {
                return new RegExp(str.replace(/\/:[^\/]*/g, '/.*'));
            };
        });
})();

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
(function() {

    /**
     * @ngdoc overview
     * @name yLoDashModule
     * @description
     * This module manages the use of the lodash library in SmartEdit. It makes sure the library is introduced
     * in the Angular lifecycle and makes it easy to mock for unit tests.
     */
    angular.module('yLoDashModule', [])
        /**
         * @ngdoc object
         * @name yLoDashModule.lodash
         * @description
         * 
         * Makes the underscore library available to SmartEdit.
         *
         * Note: original _ namespace is removed from window in order not to clash with other libraries especially in the storefront AND to enforce proper dependency injection.
         */
        /* forbiddenNameSpaces:false */
        .factory('lodash', function() {
            if (!window.smarteditLodash) {
                if (window._ && window._.noConflict) {
                    window.smarteditLodash = window._.noConflict();
                } else {
                    throw "could not find lodash library under window._ namespace";
                }
            }
            return window.smarteditLodash;
        });
})();

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
angular.module('dropdownPopulatorInterfaceModule', ['yLoDashModule'])
    /**
     * @ngdoc service
     * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
     *
     * @description
     * Interface describing the contract of a DropdownPopulator fetched through dependency injection by the
     * {@link genericEditorModule.service:GenericEditor GenericEditor} to populate the dropdowns of {@link seDropdownModule.directive:seDropdown seDropdown}.
     */
    .factory('DropdownPopulatorInterface', function(lodash) {

        var DropdownPopulatorInterface = function() {};

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#populate
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         * @description
         * Will returns a promise resolving to a list of items.
         * this method is deprecated, use {@link DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#fetchAll, fetchAll}.
         * @param {object} payload contains the field, model and additional attributes.
         * @param {object} payload.field The field descriptor from {@link genericEditorModule.service:GenericEditor GenericEditor} containing information about the dropdown.
         * @param {object} payload.model The full model being edited in {@link genericEditorModule.service:GenericEditor GenericEditor}.
         * @param {object} payload.selection The object containing the full option object that is now selected in a dropdown that we depend on (Optional, see dependsOn property in {@link seDropdownModule.directive:seDropdown seDropdown}).
         * @param {String} payload.search The search key when the user types in the dropdown (optional).
         * @returns {object} a list of objects.
         */
        DropdownPopulatorInterface.prototype.populate = function(payload) {
            return this.fetchAll(payload);
        };

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#fetchAll
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         * @deprecated
         * @description
         * Will returns a promise resolving to a list of items.
         * The items must all contain a property <b>id</b>.
         * @param {object} payload contains the field, model and additional attributes.
         * @param {String} payload.field.options The original array of options (used by {@link optionsDropdownPopulatorModule.service:optionsDropdownPopulator optionsDropdownPopulator})
         * @param {String} payload.field.uri The uri used to make a rest call to fetch data (used by {@link uriDropdownPopulatorModule.service:uriDropdownPopulator uriDropdownPopulator})
         * @param {object} payload.field The field descriptor from {@link genericEditorModule.service:GenericEditor GenericEditor} containing information about the dropdown.
         * @param {String} payload.field.dependsOn A comma separated list of attributes to include from the model when building the request params
         * @param {String} payload.field.idAttribute The name of the attribute to use when setting the id attribute
         * @param {String} payload.field.labelAttributes A list of attributes to use when setting the label attribute
         * @param {object} payload.model The full model being edited in {@link genericEditorModule.service:GenericEditor GenericEditor}.
         * @param {object} payload.selection The object containing the full option object that is now selected in a dropdown that we depend on (Optional, see dependsOn property in {@link seDropdownModule.directive:seDropdown seDropdown}).
         * @param {String} payload.search The search key when the user types in the dropdown (optional).
         * @returns {object} a list of objects.
         */
        DropdownPopulatorInterface.prototype.fetchAll = function() {};

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#fetchPage
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         *
         * @description
         * Will returns a promise resolving to a {@link Page.object:Page page} of items.
         * The items must all contain a property <b>id</b>.
         * @param {object} payload contains the field, model and additional attributes.
         * @param {object} payload.field The field descriptor from {@link genericEditorModule.service:GenericEditor GenericEditor} containing information about the dropdown.
         * @param {String} payload.field.options The original array of options (used by {@link optionsDropdownPopulatorModule.service:optionsDropdownPopulator optionsDropdownPopulator})
         * @param {String} payload.field.uri The uri used to make a rest call to fetch data (used by {@link uriDropdownPopulatorModule.service:uriDropdownPopulator uriDropdownPopulator})
         * @param {String} payload.field.dependsOn A comma separated list of attributes to include from the model when building the request params
         * @param {String} payload.field.idAttribute The name of the attribute to use when setting the id attribute
         * @param {String} payload.field.labelAttributes A list of attributes to use when setting the label attribute
         * @param {object} payload.field.params An object containing properties to append as query string while making a call.
         * @param {object} payload.model The full model being edited in {@link genericEditorModule.service:GenericEditor GenericEditor}.
         * @param {object} payload.selection The object containing the full option object that is now selected in a dropdown that we depend on (Optional, see dependsOn property in {@link seDropdownModule.directive:seDropdown seDropdown}).
         * @param {String} payload.search The search key when the user types in the dropdown (optional).
         * @param {String} payload.pageSize number of items in the page.
         * @param {String} payload.currentPage current page number.
         * @returns {object} a {@link Page.object:Page page}
         */
        DropdownPopulatorInterface.prototype.fetchPage = function() {};

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#isPaged
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         *
         * @description
         * Specifies whether this populator is meant to work in paged mode as opposed to retrieve lists. Optional, default is false
         */
        DropdownPopulatorInterface.prototype.isPaged = function() {
            return false;
        };

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#populateAttributes
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         *
         * @description
         * Populates the id and label property for each item in the list. If the label property is not already set,
         * then we use an ordered list of attributes to use when determining the label for each item.
         * @param {Array} items The array of items to set the id and label attributes on
         * @param {String} idAttribute The name of the id attribute
         * @param {Array} orderedLabelAttributes The ordered list of label attributes
         * @returns {Array} the modified list of items
         */
        DropdownPopulatorInterface.prototype.populateAttributes = function(items, idAttribute, orderedLabelAttributes) {
            return lodash.map(items, function(item) {
                if (idAttribute && lodash.isEmpty(item.id)) {
                    item.id = item[idAttribute];
                }

                if (orderedLabelAttributes && lodash.isEmpty(item.label)) {
                    // Find the first attribute that the item object contains
                    var labelAttribute = lodash.find(orderedLabelAttributes, function(attr) {
                        return !lodash.isEmpty(item[attr]);
                    });

                    // If we found an attribute, set the label
                    if (labelAttribute) {
                        item.label = item[labelAttribute];
                    }
                }

                return item;
            });
        };

        /**
         * @ngdoc method
         * @name DropdownPopulatorInterfaceModule.DropdownPopulatorInterface#search
         * @methodOf DropdownPopulatorInterfaceModule.DropdownPopulatorInterface
         *
         * @description
         * Searches a list and returns only items with a label attribute that matches the search term
         * @param {Array} items The list of items to search
         * @param {Array} searchTerm The search term to filter items by
         * @returns {Array} the filtered list of items
         */
        DropdownPopulatorInterface.prototype.search = function(items, searchTerm) {
            return lodash.filter(items, function(item) {
                return item.label && item.label.toUpperCase().indexOf(searchTerm.toUpperCase()) > -1;
            });
        };

        return DropdownPopulatorInterface;
    });

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
angular.module('templateCacheDecoratorModule', [])
    .config(function($provide) {

        var pathRegExp = /web.+\/(\w+)\.html/;
        var namePathMap = {};

        $provide.decorator('$templateCache', ['$delegate', function($delegate) {

            var originalPut = $delegate.put;

            $delegate.put = function() {
                var path = arguments[0];
                var template = arguments[1];
                if (pathRegExp.test(path)) {
                    var fileName = pathRegExp.exec(path)[1] + ".html";
                    if (!namePathMap[fileName]) {

                        originalPut.apply($delegate, [fileName, template]);
                        namePathMap[fileName] = path;
                    } else {
                        throw "[templateCacheDecorator] html templates '" + namePathMap[fileName] + "' and '" + path + "' are conflicting, you must give them different filenames";
                    }
                }
                return originalPut.apply($delegate, arguments);
            };

            // ============== UNCOMMENT THIS TO DEBUG TEMPLATECACHE ==============
            // ========================== DO NOT COMMIT ==========================
            // var originalGet = $delegate.get;
            //
            // $delegate.get = function() {
            //     var path = arguments[0];
            //     var $log = angular.injector(['ng']).get('$log');
            //
            //     $log.debug("$templateCache GET: " + path);
            //     return originalGet.apply($delegate, arguments);
            // };

            return $delegate;
        }]);
    });