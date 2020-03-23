/**
 * @licstart The following is the entire license notice for the
 * Javascript code in this page
 *
 * Copyright 2019 Mozilla Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @licend The above is the entire license notice for the
 * Javascript code in this page
 */
window.addEventListener("unhandledrejection", event => {
    if (event.reason instanceof Error) {
        console.log(event.reason.stack);
    }
    console.warn(`UNHANDLED PROMISE REJECTION: ${event.reason}`);
});
// polyfill from https://www.paulirish.com/2011/requestanimationframe-for-smart-animating/
(function() {
  let lastTime = 0;
  const vendors = ['webkit', 'moz'];
  for(let x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
    window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
    window.cancelAnimationFrame =
        window[vendors[x]+'CancelAnimationFrame'] || window[vendors[x]+'CancelRequestAnimationFrame'];
  }

  if (!window.requestAnimationFrame)
    window.requestAnimationFrame = function(callback) {
      const currTime = new Date().getTime();
      const timeToCall = Math.max(0, 16 - (currTime - lastTime));
      let id = window.setTimeout(function() { callback(currTime + timeToCall); },
          timeToCall);
      lastTime = currTime + timeToCall;
      return id;
    };

  if (!window.cancelAnimationFrame)
    window.cancelAnimationFrame = function(id) {
      clearTimeout(id);
    };
}());

(function(modules) { // webpackBootstrap
 	// The module cache

      const installedModules = {};

        // The require function
      function __webpack_require__(moduleId) {
          // Check if module is in cache
          if(installedModules[moduleId]) {
              return installedModules[moduleId].exports;
          }
            // Create a new module (and put it into the cache)
          const module = installedModules[moduleId] = {
                        i: moduleId,
                        l: false,
                        exports: {}
          };

          // Execute the module function
          modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

          // Flag the module as loaded
          module.l = true;

          // Return the exports of the module
          return module.exports;
      }


  // expose the modules object (__webpack_modules__)
  __webpack_require__.m = modules;

  // expose the module cache
  __webpack_require__.c = installedModules;

  // define getter function for harmony exports
  __webpack_require__.d = function(exports, name, getter) {
      if(!__webpack_require__.o(exports, name)) {
          Object.defineProperty(exports, name, { enumerable: true, get: getter });
      }
  };

  // define __esModule on exports
  __webpack_require__.r = function(exports) {
      if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
          Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
      }
      Object.defineProperty(exports, '__esModule', { value: true });
  };

  // create a fake namespace object
  // mode & 1: value is a module id, require it
  // mode & 2: merge all properties of value into the ns
  // mode & 4: return value when already ns object
  // mode & 8|1: behave like require
  __webpack_require__.t = function(value, mode) {
      if(mode & 1) value = __webpack_require__(value);
      if(mode & 8) return value;
      if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;

  const ns = Object.create(null);
          __webpack_require__.r(ns);
      Object.defineProperty(ns, 'default', { enumerable: true, value: value });
      if(mode & 2 && typeof value != 'string') for(let key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
      return ns;
  };

  // getDefaultExport function for compatibility with non-harmony modules
  __webpack_require__.n = function(module) {

  const getter = module && module.__esModule ?
                  function getDefault() {
        return module['default'];
      } :
                  function getModuleExports() {
        return module;
      };
          __webpack_require__.d(getter, 'a', getter);
      return getter;
  };

  // Object.prototype.hasOwnProperty.call
  __webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };

  // __webpack_public_path__
  __webpack_require__.p = "";


  // Load entry module and return exports
  return __webpack_require__(__webpack_require__.s = 0);
})
/************************************************************************/
 ([
/* 0 */
(function(module, exports, __webpack_require__) {
    "use strict";
    let pdfjsWebApp, pdfjsWebAppOptions;
    {
      pdfjsWebApp = __webpack_require__(1);
      pdfjsWebAppOptions = __webpack_require__(6);
    }
    ;
    {
      __webpack_require__(36);
    }
    ;
    {
      __webpack_require__(41);
    }

    function getViewerConfiguration() {
         return {
              appContainer: document.body,
              mainContainer: document.getElementById('viewerContainer'),
              viewerContainer: document.getElementById('viewer'),
              eventBus: null,
              toolbar: {
                container: document.getElementById('toolbarViewer'),
                numPages: document.getElementById('numPages'),
                pageNumber: document.getElementById('pageNumber'),
                scaleSelectContainer: document.getElementById('scaleSelectContainer'),
                scaleSelect: document.getElementById('scaleSelect'),
                customScaleOption: document.getElementById('customScaleOption'),
                previous: document.getElementById('previous'),
                next: document.getElementById('next'),
                zoomIn: document.getElementById('zoomIn'),
                zoomOut: document.getElementById('zoomOut'),
                viewFind: document.getElementById('viewFind'),
                openFile: document.getElementById('openFile'),
                print: document.getElementById('print'),
                presentationModeButton: document.getElementById('presentationMode'),
                download: document.getElementById('download'),
                viewBookmark: document.getElementById('viewBookmark')
              },
              secondaryToolbar: {
                toolbar: document.getElementById('secondaryToolbar'),
                toggleButton: document.getElementById('secondaryToolbarToggle'),
                toolbarButtonContainer: document.getElementById('secondaryToolbarButtonContainer'),
                presentationModeButton: document.getElementById('secondaryPresentationMode'),
                openFileButton: document.getElementById('secondaryOpenFile'),
                printButton: document.getElementById('secondaryPrint'),
                downloadButton: document.getElementById('secondaryDownload'),
                viewBookmarkButton: document.getElementById('secondaryViewBookmark'),
                firstPageButton: document.getElementById('firstPage'),
                lastPageButton: document.getElementById('lastPage'),
                pageRotateCwButton: document.getElementById('pageRotateCw'),
                pageRotateCcwButton: document.getElementById('pageRotateCcw'),
                cursorSelectToolButton: document.getElementById('cursorSelectTool'),
                cursorHandToolButton: document.getElementById('cursorHandTool'),
                scrollVerticalButton: document.getElementById('scrollVertical'),
                scrollHorizontalButton: document.getElementById('scrollHorizontal'),
                scrollWrappedButton: document.getElementById('scrollWrapped'),
                spreadNoneButton: document.getElementById('spreadNone'),
                spreadOddButton: document.getElementById('spreadOdd'),
                spreadEvenButton: document.getElementById('spreadEven'),
                documentPropertiesButton: document.getElementById('documentProperties')
              },
              fullscreen: {
                contextFirstPage: document.getElementById('contextFirstPage'),
                contextLastPage: document.getElementById('contextLastPage'),
                contextPageRotateCw: document.getElementById('contextPageRotateCw'),
                contextPageRotateCcw: document.getElementById('contextPageRotateCcw')
              },
              sidebar: {
                outerContainer: document.getElementById('outerContainer'),
                viewerContainer: document.getElementById('viewerContainer'),
                toggleButton: document.getElementById('sidebarToggle'),
                thumbnailButton: document.getElementById('viewThumbnail'),
                outlineButton: document.getElementById('viewOutline'),
                attachmentsButton: document.getElementById('viewAttachments'),
                thumbnailView: document.getElementById('thumbnailView'),
                outlineView: document.getElementById('outlineView'),
                attachmentsView: document.getElementById('attachmentsView')
              },
              sidebarResizer: {
                outerContainer: document.getElementById('outerContainer'),
                resizer: document.getElementById('sidebarResizer')
              },
              findBar: {
                bar: document.getElementById('findbar'),
                toggleButton: document.getElementById('viewFind'),
                findField: document.getElementById('findInput'),
                highlightAllCheckbox: document.getElementById('findHighlightAll'),
                caseSensitiveCheckbox: document.getElementById('findMatchCase'),
                entireWordCheckbox: document.getElementById('findEntireWord'),
                findMsg: document.getElementById('findMsg'),
                findResultsCount: document.getElementById('findResultsCount'),
                findPreviousButton: document.getElementById('findPrevious'),
                findNextButton: document.getElementById('findNext')
              },
              passwordOverlay: {
                overlayName: 'passwordOverlay',
                container: document.getElementById('passwordOverlay'),
                label: document.getElementById('passwordText'),
                input: document.getElementById('password'),
                submitButton: document.getElementById('passwordSubmit'),
                cancelButton: document.getElementById('passwordCancel')
              },
              documentProperties: {
                overlayName: 'documentPropertiesOverlay',
                container: document.getElementById('documentPropertiesOverlay'),
                closeButton: document.getElementById('documentPropertiesClose'),
                fields: {
                  'fileName': document.getElementById('fileNameField'),
                  'fileSize': document.getElementById('fileSizeField'),
                  'title': document.getElementById('titleField'),
                  'author': document.getElementById('authorField'),
                  'subject': document.getElementById('subjectField'),
                  'keywords': document.getElementById('keywordsField'),
                  'creationDate': document.getElementById('creationDateField'),
                  'modificationDate': document.getElementById('modificationDateField'),
                  'creator': document.getElementById('creatorField'),
                  'producer': document.getElementById('producerField'),
                  'version': document.getElementById('versionField'),
                  'pageCount': document.getElementById('pageCountField'),
                  'pageSize': document.getElementById('pageSizeField'),
                  'linearized': document.getElementById('linearizedField')
                }
              },
              errorWrapper: {
                container: document.getElementById('errorWrapper'),
                errorMessage: document.getElementById('errorMessage'),
                closeButton: document.getElementById('errorClose'),
                errorMoreInfo: document.getElementById('errorMoreInfo'),
                moreInfoButton: document.getElementById('errorShowMore'),
                lessInfoButton: document.getElementById('errorShowLess')
              },
              printContainer: document.getElementById('printContainer'),
              openFileInputName: 'fileInput',
              debuggerScriptPath: './debugger.js'
            };
    }

function webViewerLoad() {
  const config = getViewerConfiguration();
  window.PDFViewerApplication = pdfjsWebApp.PDFViewerApplication;
  window.PDFViewerApplicationOptions = pdfjsWebAppOptions.AppOptions;
  const event = document.createEvent('CustomEvent');
  event.initCustomEvent('webviewerloaded', true, true, {});
  document.dispatchEvent(event);
  pdfjsWebApp.PDFViewerApplication.run(config);
}

if (document.readyState === 'interactive' || document.readyState === 'complete') {
  webViewerLoad();
} else {
  document.addEventListener('DOMContentLoaded', webViewerLoad, true);
}

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFPrintServiceFactory = exports.DefaultExternalServices = exports.PDFViewerApplication = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        const _ui_utils = __webpack_require__(5);

        const _app_options = __webpack_require__(6);

        const _pdfjsLib = __webpack_require__(7);

        const _pdf_cursor_tools = __webpack_require__(9);

        const _pdf_rendering_queue = __webpack_require__(11);

        const _pdf_sidebar = __webpack_require__(12);

        const _overlay_manager = __webpack_require__(13);

        const _password_prompt = __webpack_require__(14);

        const _pdf_attachment_viewer = __webpack_require__(15);

        const _pdf_document_properties = __webpack_require__(16);

        const _pdf_find_bar = __webpack_require__(17);

        const _pdf_find_controller = __webpack_require__(18);

        const _pdf_history = __webpack_require__(20);

        const _pdf_link_service = __webpack_require__(21);

        const _pdf_outline_viewer = __webpack_require__(22);

        const _pdf_presentation_mode = __webpack_require__(23);

        const _pdf_sidebar_resizer = __webpack_require__(24);

        const _pdf_thumbnail_viewer = __webpack_require__(25);

        const _pdf_viewer = __webpack_require__(27);

        const _secondary_toolbar = __webpack_require__(32);

        const _toolbar = __webpack_require__(34);

        const _view_history = __webpack_require__(35);

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) { return; }
  const _arr = [];
  let _n = true;
  let _d = false;
  let _e = undefined;
  try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

        const DEFAULT_SCALE_DELTA = 1.1;
        const DISABLE_AUTO_FETCH_LOADING_BAR_TIMEOUT = 5000;
        const FORCE_PAGES_LOADED_TIMEOUT = 10000;
        const WHEEL_ZOOM_DISABLED_TIMEOUT = 1000;
        const ViewOnLoad = {
          UNKNOWN: -1,
          PREVIOUS: 0,
          INITIAL: 1
        };
        const DefaultExternalServices = {
          updateFindControlState: function updateFindControlState(data) {
          },
          updateFindMatchesCount: function updateFindMatchesCount(data) {
          },
          initPassiveLoading: function initPassiveLoading(callbacks) {
          },
          fallback: function fallback(data, callback) {
          },
          reportTelemetry: function reportTelemetry(data) {
          },
          createDownloadManager: function createDownloadManager(options) {
            throw new Error('Not implemented: createDownloadManager');
          },
          createPreferences: function createPreferences() {
            throw new Error('Not implemented: createPreferences');
          },
          createL10n: function createL10n(options) {
            throw new Error('Not implemented: createL10n');
          },
          supportsIntegratedFind: false,
          supportsDocumentFonts: true,
          supportsDocumentColors: true,
          supportedMouseWheelZoomModifierKeys: {
            ctrlKey: true,
            metaKey: true
          }
        };
        exports.DefaultExternalServices = DefaultExternalServices;
        const PDFViewerApplication = {
          initialBookmark: document.location.hash.substring(1),
          initialized: false,
          fellback: false,
          appConfig: null,
          pdfDocument: null,
          pdfLoadingTask: null,
          printService: null,
          pdfViewer: null,
          pdfThumbnailViewer: null,
          pdfRenderingQueue: null,
          pdfPresentationMode: null,
          pdfDocumentProperties: null,
          pdfLinkService: null,
          pdfHistory: null,
          pdfSidebar: null,
          pdfSidebarResizer: null,
          pdfOutlineViewer: null,
          pdfAttachmentViewer: null,
          pdfCursorTools: null,
          store: null,
          downloadManager: null,
          overlayManager: null,
          preferences: null,
          toolbar: null,
          secondaryToolbar: null,
          eventBus: null,
          l10n: null,
          isInitialViewSet: false,
          downloadComplete: false,
          isViewerEmbedded: window.parent !== window,
          url: '',
          baseUrl: '',
          externalServices: DefaultExternalServices,
          _boundEvents: {},
          contentDispositionFilename: null,
          initialize: function () {
            const _initialize = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee(appConfig) {
                  const _this = this;

                  let appContainer;
                  return _regenerator["default"].wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          this.preferences = this.externalServices.createPreferences();
                          this.appConfig = appConfig;
                          _context.next = 4;
                          return this._readPreferences();

                        case 4:
                          _context.next = 6;
                          return this._parseHashParameters();

                        case 6:
                          _context.next = 8;
                          return this._initializeL10n();

                        case 8:
                          if (this.isViewerEmbedded && _app_options.AppOptions.get('externalLinkTarget') === _pdfjsLib.LinkTarget.NONE) {
                            _app_options.AppOptions.set('externalLinkTarget', _pdfjsLib.LinkTarget.TOP);
                          }

                          _context.next = 11;
                          return this._initializeViewerComponents();

                        case 11:
                          this.bindEvents();
                          this.bindWindowEvents();
                          appContainer = appConfig.appContainer || document.documentElement;
                          this.l10n.translate(appContainer).then(function () {
                            _this.eventBus.dispatch('localized', {
                              source: _this
                            });
                          });
                          this.initialized = true;

                        case 16:
                        case "end":
                          return _context.stop();
                      }
                    }
                  }, _callee, this);
                }));

            function initialize(_x) {
              return _initialize.apply(this, arguments);
            }

            return initialize;
          }(),
          _readPreferences: function () {
            const _readPreferences2 = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee2() {
                  let prefs, name;
                  return _regenerator["default"].wrap(function _callee2$(_context2) {
                    while (1) {
                      switch (_context2.prev = _context2.next) {
                        case 0:
                          if (!(_app_options.AppOptions.get('disablePreferences') === true)) {
                            _context2.next = 2;
                            break;
                          }

                          return _context2.abrupt("return");

                        case 2:
                          _context2.prev = 2;
                          _context2.next = 5;
                          return this.preferences.getAll();

                        case 5:
                          prefs = _context2.sent;

                          for (name in prefs) {
                            _app_options.AppOptions.set(name, prefs[name]);
                          }

                          _context2.next = 12;
                          break;

                        case 9:
                          _context2.prev = 9;
                          _context2.t0 = _context2["catch"](2);
                          console.error("_readPreferences: \"".concat(_context2.t0.message, "\"."));

                        case 12:
                        case "end":
                          return _context2.stop();
                      }
                    }
                  }, _callee2, this, [[2, 9]]);
                }));

            function _readPreferences() {
              return _readPreferences2.apply(this, arguments);
            }

            return _readPreferences;
          }(),
          _parseHashParameters: function () {
            const _parseHashParameters2 = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee3() {
                  let waitOn, hash, hashParams, viewer, enabled;
                  return _regenerator["default"].wrap(function _callee3$(_context3) {
                    while (1) {
                      switch (_context3.prev = _context3.next) {
                        case 0:
                          if (_app_options.AppOptions.get('pdfBugEnabled')) {
                            _context3.next = 2;
                            break;
                          }

                          return _context3.abrupt("return", undefined);

                        case 2:
                          waitOn = [];
                          hash = document.location.hash.substring(1);
                          hashParams = (0, _ui_utils.parseQueryString)(hash);

                          if ('disableworker' in hashParams && hashParams['disableworker'] === 'true') {
                            waitOn.push(loadFakeWorker());
                          }

                          if ('disablerange' in hashParams) {
                            _app_options.AppOptions.set('disableRange', hashParams['disablerange'] === 'true');
                          }

                          if ('disablestream' in hashParams) {
                            _app_options.AppOptions.set('disableStream', hashParams['disablestream'] === 'true');
                          }

                          if ('disableautofetch' in hashParams) {
                            _app_options.AppOptions.set('disableAutoFetch', hashParams['disableautofetch'] === 'true');
                          }

                          if ('disablefontface' in hashParams) {
                            _app_options.AppOptions.set('disableFontFace', hashParams['disablefontface'] === 'true');
                          }

                          if ('disablehistory' in hashParams) {
                            _app_options.AppOptions.set('disableHistory', hashParams['disablehistory'] === 'true');
                          }

                          if ('webgl' in hashParams) {
                            _app_options.AppOptions.set('enableWebGL', hashParams['webgl'] === 'true');
                          }

                          if ('useonlycsszoom' in hashParams) {
                            _app_options.AppOptions.set('useOnlyCssZoom', hashParams['useonlycsszoom'] === 'true');
                          }

                          if ('verbosity' in hashParams) {
                            _app_options.AppOptions.set('verbosity', hashParams['verbosity'] | 0);
                          }

                          if (!('textlayer' in hashParams)) {
                            _context3.next = 23;
                            break;
                          }

                          _context3.t0 = hashParams['textlayer'];
                          _context3.next = _context3.t0 === 'off' ? 18 : _context3.t0 === 'visible' ? 20 : _context3.t0 === 'shadow' ? 20 : _context3.t0 === 'hover' ? 20 : 23;
                          break;

                        case 18:
                          _app_options.AppOptions.set('textLayerMode', _ui_utils.TextLayerMode.DISABLE);

                          return _context3.abrupt("break", 23);

                        case 20:
                          viewer = this.appConfig.viewerContainer;
                          viewer.classList.add('textLayer-' + hashParams['textlayer']);
                          return _context3.abrupt("break", 23);

                        case 23:
                          if ('pdfbug' in hashParams) {
                            _app_options.AppOptions.set('pdfBug', true);

                            enabled = hashParams['pdfbug'].split(',');
                            waitOn.push(loadAndEnablePDFBug(enabled));
                          }

                          if ('locale' in hashParams) {
                            _app_options.AppOptions.set('locale', hashParams['locale']);
                          }

                          return _context3.abrupt("return", Promise.all(waitOn)["catch"](function (reason) {
                            console.error("_parseHashParameters: \"".concat(reason.message, "\"."));
                          }));

                        case 26:
                        case "end":
                          return _context3.stop();
                      }
                    }
                  }, _callee3, this);
                }));

            function _parseHashParameters() {
              return _parseHashParameters2.apply(this, arguments);
            }

            return _parseHashParameters;
          }(),
          _initializeL10n: function () {
            const _initializeL10n2 = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee4() {
                  let dir;
                  return _regenerator["default"].wrap(function _callee4$(_context4) {
                    while (1) {
                      switch (_context4.prev = _context4.next) {
                        case 0:
                          this.l10n = this.externalServices.createL10n({
                            locale: _app_options.AppOptions.get('locale')
                          });
                          _context4.next = 3;
                          return this.l10n.getDirection();

                        case 3:
                          dir = _context4.sent;
                          document.getElementsByTagName('html')[0].dir = dir;

                        case 5:
                        case "end":
                          return _context4.stop();
                      }
                    }
                  }, _callee4, this);
                }));

            function _initializeL10n() {
              return _initializeL10n2.apply(this, arguments);
            }

            return _initializeL10n;
          }(),
          _initializeViewerComponents: function () {
            const _initializeViewerComponents2 = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee5() {
                  let appConfig, eventBus, pdfRenderingQueue, pdfLinkService, downloadManager, findController,
                      container,
                      viewer;
                  return _regenerator["default"].wrap(function _callee5$(_context5) {
                    while (1) {
                      switch (_context5.prev = _context5.next) {
                        case 0:
                          appConfig = this.appConfig;
                          this.overlayManager = new _overlay_manager.OverlayManager();
                          eventBus = appConfig.eventBus || (0, _ui_utils.getGlobalEventBus)(_app_options.AppOptions.get('eventBusDispatchToDOM'));
                          this.eventBus = eventBus;
                          pdfRenderingQueue = new _pdf_rendering_queue.PDFRenderingQueue();
                          pdfRenderingQueue.onIdle = this.cleanup.bind(this);
                          this.pdfRenderingQueue = pdfRenderingQueue;
                          pdfLinkService = new _pdf_link_service.PDFLinkService({
                            eventBus: eventBus,
                            externalLinkTarget: _app_options.AppOptions.get('externalLinkTarget'),
                            externalLinkRel: _app_options.AppOptions.get('externalLinkRel')
                          });
                          this.pdfLinkService = pdfLinkService;
                          downloadManager = this.externalServices.createDownloadManager({
                            disableCreateObjectURL: _app_options.AppOptions.get('disableCreateObjectURL')
                          });
                          this.downloadManager = downloadManager;
                          findController = new _pdf_find_controller.PDFFindController({
                            linkService: pdfLinkService,
                            eventBus: eventBus
                          });
                          this.findController = findController;
                          container = appConfig.mainContainer;
                          viewer = appConfig.viewerContainer;
                          this.pdfViewer = new _pdf_viewer.PDFViewer({
                            container: container,
                            viewer: viewer,
                            eventBus: eventBus,
                            renderingQueue: pdfRenderingQueue,
                            linkService: pdfLinkService,
                            downloadManager: downloadManager,
                            findController: findController,
                            renderer: _app_options.AppOptions.get('renderer'),
                            enableWebGL: _app_options.AppOptions.get('enableWebGL'),
                            l10n: this.l10n,
                            textLayerMode: _app_options.AppOptions.get('textLayerMode'),
                            imageResourcesPath: _app_options.AppOptions.get('imageResourcesPath'),
                            renderInteractiveForms: _app_options.AppOptions.get('renderInteractiveForms'),
                            enablePrintAutoRotate: _app_options.AppOptions.get('enablePrintAutoRotate'),
                            useOnlyCssZoom: _app_options.AppOptions.get('useOnlyCssZoom'),
                            maxCanvasPixels: _app_options.AppOptions.get('maxCanvasPixels')
                          });
                          pdfRenderingQueue.setViewer(this.pdfViewer);
                          pdfLinkService.setViewer(this.pdfViewer);
                          this.pdfThumbnailViewer = new _pdf_thumbnail_viewer.PDFThumbnailViewer({
                            container: appConfig.sidebar.thumbnailView,
                            renderingQueue: pdfRenderingQueue,
                            linkService: pdfLinkService,
                            l10n: this.l10n
                          });
                          pdfRenderingQueue.setThumbnailViewer(this.pdfThumbnailViewer);
                          this.pdfHistory = new _pdf_history.PDFHistory({
                            linkService: pdfLinkService,
                            eventBus: eventBus
                          });
                          pdfLinkService.setHistory(this.pdfHistory);

                          if (!this.supportsIntegratedFind) {
                            this.findBar = new _pdf_find_bar.PDFFindBar(appConfig.findBar, eventBus, this.l10n);
                          }

                          this.pdfDocumentProperties = new _pdf_document_properties.PDFDocumentProperties(appConfig.documentProperties, this.overlayManager, eventBus, this.l10n);
                          this.pdfCursorTools = new _pdf_cursor_tools.PDFCursorTools({
                            container: container,
                            eventBus: eventBus,
                            cursorToolOnLoad: _app_options.AppOptions.get('cursorToolOnLoad')
                          });
                          this.toolbar = new _toolbar.Toolbar(appConfig.toolbar, eventBus, this.l10n);
                          this.secondaryToolbar = new _secondary_toolbar.SecondaryToolbar(appConfig.secondaryToolbar, container, eventBus);

                          if (this.supportsFullscreen) {
                            this.pdfPresentationMode = new _pdf_presentation_mode.PDFPresentationMode({
                              container: container,
                              viewer: viewer,
                              pdfViewer: this.pdfViewer,
                              eventBus: eventBus,
                              contextMenuItems: appConfig.fullscreen
                            });
                          }

                          this.passwordPrompt = new _password_prompt.PasswordPrompt(appConfig.passwordOverlay, this.overlayManager, this.l10n);
                          this.pdfOutlineViewer = new _pdf_outline_viewer.PDFOutlineViewer({
                            container: appConfig.sidebar.outlineView,
                            eventBus: eventBus,
                            linkService: pdfLinkService
                          });
                          this.pdfAttachmentViewer = new _pdf_attachment_viewer.PDFAttachmentViewer({
                            container: appConfig.sidebar.attachmentsView,
                            eventBus: eventBus,
                            downloadManager: downloadManager
                          });
                          this.pdfSidebar = new _pdf_sidebar.PDFSidebar({
                            elements: appConfig.sidebar,
                            pdfViewer: this.pdfViewer,
                            pdfThumbnailViewer: this.pdfThumbnailViewer,
                            eventBus: eventBus,
                            l10n: this.l10n
                          });
                          this.pdfSidebar.onToggled = this.forceRendering.bind(this);
                          this.pdfSidebarResizer = new _pdf_sidebar_resizer.PDFSidebarResizer(appConfig.sidebarResizer, eventBus, this.l10n);

                        case 34:
                        case "end":
                          return _context5.stop();
                      }
                    }
                  }, _callee5, this);
                }));

            function _initializeViewerComponents() {
              return _initializeViewerComponents2.apply(this, arguments);
            }

            return _initializeViewerComponents;
          }(),
          run: function run(config) {
            this.initialize(config).then(webViewerInitialized);
          },
          zoomIn: function zoomIn(ticks) {
            if (this.pdfViewer.isInPresentationMode) {
              return;
            }

            let newScale = this.pdfViewer.currentScale;

            do {
              newScale = (newScale * DEFAULT_SCALE_DELTA).toFixed(2);
              newScale = Math.ceil(newScale * 10) / 10;
              newScale = Math.min(_ui_utils.MAX_SCALE, newScale);
            } while (--ticks > 0 && newScale < _ui_utils.MAX_SCALE);

            this.pdfViewer.currentScaleValue = newScale;
          },
          zoomOut: function zoomOut(ticks) {
            if (this.pdfViewer.isInPresentationMode) {
              return;
            }

            let newScale = this.pdfViewer.currentScale;

            do {
              newScale = (newScale / DEFAULT_SCALE_DELTA).toFixed(2);
              newScale = Math.floor(newScale * 10) / 10;
              newScale = Math.max(_ui_utils.MIN_SCALE, newScale);
            } while (--ticks > 0 && newScale > _ui_utils.MIN_SCALE);

            this.pdfViewer.currentScaleValue = newScale;
          },
          zoomReset: function zoomReset() {
            if (this.pdfViewer.isInPresentationMode) {
              return;
            }

            this.pdfViewer.currentScaleValue = _ui_utils.DEFAULT_SCALE_VALUE;
          },

          get pagesCount() {
            return this.pdfDocument ? this.pdfDocument.numPages : 0;
          },

          set page(val) {
            this.pdfViewer.currentPageNumber = val;
          },

          get page() {
            return this.pdfViewer.currentPageNumber;
          },

          get printing() {
            return !!this.printService;
          },

          get supportsPrinting() {
            return PDFPrintServiceFactory.instance.supportsPrinting;
          },

          get supportsFullscreen() {
            let support;
            const doc = document.documentElement;
            support = !!(doc.requestFullscreen || doc.mozRequestFullScreen || doc.webkitRequestFullScreen || doc.msRequestFullscreen);

            if (document.fullscreenEnabled === false || document.mozFullScreenEnabled === false || document.webkitFullscreenEnabled === false || document.msFullscreenEnabled === false) {
              support = false;
            }

            return (0, _pdfjsLib.shadow)(this, 'supportsFullscreen', support);
          },

          get supportsIntegratedFind() {
            return this.externalServices.supportsIntegratedFind;
          },

          get supportsDocumentFonts() {
            return this.externalServices.supportsDocumentFonts;
          },

          get supportsDocumentColors() {
            return this.externalServices.supportsDocumentColors;
          },

          get loadingBar() {
            const bar = new _ui_utils.ProgressBar('#loadingBar');
            return (0, _pdfjsLib.shadow)(this, 'loadingBar', bar);
          },

          get supportedMouseWheelZoomModifierKeys() {
            return this.externalServices.supportedMouseWheelZoomModifierKeys;
          },

          initPassiveLoading: function initPassiveLoading() {
            throw new Error('Not implemented: initPassiveLoading');
          },
          setTitleUsingUrl: function setTitleUsingUrl() {
            const url = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '';
            this.url = url;
            this.baseUrl = url.split('#')[0];
            let title = (0, _ui_utils.getPDFFileNameFromURL)(url, '');

            if (!title) {
              try {
                title = decodeURIComponent((0, _pdfjsLib.getFilenameFromUrl)(url)) || url;
              } catch (ex) {
                title = url;
              }
            }

            this.setTitle(title);
          },
          setTitle: function setTitle(title) {
            if (this.isViewerEmbedded) {
              return;
            }

            document.title = title;
          },
          close: function () {
            const _close = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee6() {
                  let errorWrapper, promise;
                  return _regenerator["default"].wrap(function _callee6$(_context6) {
                    while (1) {
                      switch (_context6.prev = _context6.next) {
                        case 0:
                          errorWrapper = this.appConfig.errorWrapper.container;
                          errorWrapper.setAttribute('hidden', 'true');

                          if (this.pdfLoadingTask) {
                            _context6.next = 4;
                            break;
                          }

                          return _context6.abrupt("return", undefined);

                        case 4:
                          promise = this.pdfLoadingTask.destroy();
                          this.pdfLoadingTask = null;

                          if (this.pdfDocument) {
                            this.pdfDocument = null;
                            this.pdfThumbnailViewer.setDocument(null);
                            this.pdfViewer.setDocument(null);
                            this.pdfLinkService.setDocument(null);
                            this.pdfDocumentProperties.setDocument(null);
                          }

                          this.store = null;
                          this.isInitialViewSet = false;
                          this.downloadComplete = false;
                          this.url = '';
                          this.baseUrl = '';
                          this.contentDispositionFilename = null;
                          this.pdfSidebar.reset();
                          this.pdfOutlineViewer.reset();
                          this.pdfAttachmentViewer.reset();

                          if (this.findBar) {
                            this.findBar.reset();
                          }

                          this.toolbar.reset();
                          this.secondaryToolbar.reset();

                          if (typeof PDFBug !== 'undefined') {
                            PDFBug.cleanup();
                          }

                          return _context6.abrupt("return", promise);

                        case 21:
                        case "end":
                          return _context6.stop();
                      }
                    }
                  }, _callee6, this);
                }));

            function close() {
              return _close.apply(this, arguments);
            }

            return close;
          }(),
          open: function () {
              console.log("arguments: " + JSON.stringify(arguments));
              const _open = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee7(file, args) {
                    console.log(`arguments in generated: ${typeof file}-${typeof args} ${JSON.stringify(arguments)}`);
                  const _this2 = this;

                  let workerParameters, key, parameters, apiParameters, _key, value, _key2, _value, loadingTask;

                  return _regenerator["default"].wrap(function _callee7$(_context7) {
                    while (1) {
                      switch (_context7.prev = _context7.next) {
                        case 0:
                          if (!this.pdfLoadingTask) {
                            _context7.next = 3;
                            break;
                          }

                          _context7.next = 3;
                          return this.close();

                        case 3:
                          workerParameters = _app_options.AppOptions.getAll(_app_options.OptionKind.WORKER);

                          for (key in workerParameters) {
                            _pdfjsLib.GlobalWorkerOptions[key] = workerParameters[key];
                          }

                          parameters = Object.create(null);

                          if (typeof file === 'string') {
                            this.setTitleUsingUrl(file);
                            parameters.url = file;
                          } else if (file && 'byteLength' in file) {
                            parameters.data = file;
                          } else if (file.url && file.originalUrl) {
                            this.setTitleUsingUrl(file.originalUrl);
                            parameters.url = file.url;
                          }

                          apiParameters = _app_options.AppOptions.getAll(_app_options.OptionKind.API);

                          for (_key in apiParameters) {
                            value = apiParameters[_key];
                            parameters[_key] = value;
                          }

                          if (args) {
                            for (_key2 in args) {
                              _value = args[_key2];

                              if (_key2 === 'length') {
                                this.pdfDocumentProperties.setFileSize(_value);
                              }

                              parameters[_key2] = _value;
                            }
                          }

                          loadingTask = _pdfjsLib.getDocument(parameters);
                          this.pdfLoadingTask = loadingTask;

                          loadingTask.onPassword = function (updateCallback, reason) {
                            _this2.pdfLinkService.externalLinkEnabled = false;

                            _this2.passwordPrompt.setUpdateCallback(updateCallback, reason);

                            _this2.passwordPrompt.open();
                          };

                          loadingTask.onProgress = function (_ref) {
                            const loaded = _ref.loaded,
                                total = _ref.total;

                            _this2.progress(loaded / total);
                          };

                          loadingTask.onUnsupportedFeature = this.fallback.bind(this);
                          return _context7.abrupt("return", loadingTask.promise.then(function (pdfDocument) {
                            _this2.load(pdfDocument);
                          }, function (exception) {
                            if (loadingTask !== _this2.pdfLoadingTask) {
                              return undefined;
                            }

                            const message = exception && exception.message;
                            let loadingErrorMessage;

                            if (exception instanceof _pdfjsLib.InvalidPDFException) {
                              loadingErrorMessage = _this2.l10n.get('invalid_file_error', null, 'Invalid or corrupted PDF file.');
                            } else if (exception instanceof _pdfjsLib.MissingPDFException) {
                              loadingErrorMessage = _this2.l10n.get('missing_file_error', null, 'Missing PDF file.');
                            } else if (exception instanceof _pdfjsLib.UnexpectedResponseException) {
                              loadingErrorMessage = _this2.l10n.get('unexpected_response_error', null, 'Unexpected server response.');
                            } else {
                              loadingErrorMessage = _this2.l10n.get('loading_error', null, 'An error occurred while loading the PDF.');
                            }

                            return loadingErrorMessage.then(function (msg) {
                              _this2.error(msg, {
                                message: message
                              });

                              throw new Error(msg);
                            });
                          }));

                        case 16:
                        case "end":
                          return _context7.stop();
                      }
                    }
                  }, _callee7, this);
                }));

            function open(_x2, _x3) {
              return _open.apply(this, arguments);
            }

            return open;
          }(),
          download: function download() {
            const _this3 = this;

            function downloadByUrl() {
              downloadManager.downloadUrl(url, filename);
            }

            var url = this.baseUrl;
            var filename = this.contentDispositionFilename || (0, _ui_utils.getPDFFileNameFromURL)(this.url);
            var downloadManager = this.downloadManager;

            downloadManager.onerror = function (err) {
              _this3.error("PDF failed to download: ".concat(err));
            };

            if (!this.pdfDocument || !this.downloadComplete) {
              downloadByUrl();
              return;
            }

            this.pdfDocument.getData().then(function (data) {
              const blob = new Blob([data], {
                type: 'application/pdf'
              });
              downloadManager.download(blob, url, filename);
            })["catch"](downloadByUrl);
          },
          fallback: function fallback(featureId) {
          },
          error: function error(message, moreInfo) {
            const moreInfoText = [this.l10n.get('error_version_info', {
              version: _pdfjsLib.version || '?',
              build: _pdfjsLib.build || '?'
            }, 'PDF.js v{{version}} (build: {{build}})')];

            if (moreInfo) {
              moreInfoText.push(this.l10n.get('error_message', {
                message: moreInfo.message
              }, 'Message: {{message}}'));

              if (moreInfo.stack) {
                moreInfoText.push(this.l10n.get('error_stack', {
                  stack: moreInfo.stack
                }, 'Stack: {{stack}}'));
              } else {
                if (moreInfo.filename) {
                  moreInfoText.push(this.l10n.get('error_file', {
                    file: moreInfo.filename
                  }, 'File: {{file}}'));
                }

                if (moreInfo.lineNumber) {
                  moreInfoText.push(this.l10n.get('error_line', {
                    line: moreInfo.lineNumber
                  }, 'Line: {{line}}'));
                }
              }
            }

            const errorWrapperConfig = this.appConfig.errorWrapper;
            const errorWrapper = errorWrapperConfig.container;
            errorWrapper.removeAttribute('hidden');
            const errorMessage = errorWrapperConfig.errorMessage;
            errorMessage.textContent = message;
            const closeButton = errorWrapperConfig.closeButton;

            closeButton.onclick = function () {
              errorWrapper.setAttribute('hidden', 'true');
            };

            const errorMoreInfo = errorWrapperConfig.errorMoreInfo;
            const moreInfoButton = errorWrapperConfig.moreInfoButton;
            const lessInfoButton = errorWrapperConfig.lessInfoButton;

            moreInfoButton.onclick = function () {
              errorMoreInfo.removeAttribute('hidden');
              moreInfoButton.setAttribute('hidden', 'true');
              lessInfoButton.removeAttribute('hidden');
              errorMoreInfo.style.height = errorMoreInfo.scrollHeight + 'px';
            };

            lessInfoButton.onclick = function () {
              errorMoreInfo.setAttribute('hidden', 'true');
              moreInfoButton.removeAttribute('hidden');
              lessInfoButton.setAttribute('hidden', 'true');
            };

            moreInfoButton.oncontextmenu = _ui_utils.noContextMenuHandler;
            lessInfoButton.oncontextmenu = _ui_utils.noContextMenuHandler;
            closeButton.oncontextmenu = _ui_utils.noContextMenuHandler;
            moreInfoButton.removeAttribute('hidden');
            lessInfoButton.setAttribute('hidden', 'true');
            Promise.all(moreInfoText).then(function (parts) {
              errorMoreInfo.value = parts.join('\n');
            });
          },
          progress: function progress(level) {
            const _this4 = this;

            if (this.downloadComplete) {
              return;
            }

            const percent = Math.round(level * 100);

            if (percent > this.loadingBar.percent || isNaN(percent)) {
              this.loadingBar.percent = percent;
              const disableAutoFetch = this.pdfDocument ? this.pdfDocument.loadingParams['disableAutoFetch'] : _app_options.AppOptions.get('disableAutoFetch');

              if (disableAutoFetch && percent) {
                if (this.disableAutoFetchLoadingBarTimeout) {
                  clearTimeout(this.disableAutoFetchLoadingBarTimeout);
                  this.disableAutoFetchLoadingBarTimeout = null;
                }

                this.loadingBar.show();
                this.disableAutoFetchLoadingBarTimeout = setTimeout(function () {
                  _this4.loadingBar.hide();

                  _this4.disableAutoFetchLoadingBarTimeout = null;
                }, DISABLE_AUTO_FETCH_LOADING_BAR_TIMEOUT);
              }
            }
          },
          load: function load(pdfDocument) {
            const _this5 = this;

            this.pdfDocument = pdfDocument;
            pdfDocument.getDownloadInfo().then(function () {
              _this5.downloadComplete = true;

              _this5.loadingBar.hide();

              firstPagePromise.then(function () {
                _this5.eventBus.dispatch('documentloaded', {
                  source: _this5
                });
              });
            });
            const pageLayoutPromise = pdfDocument.getPageLayout()["catch"](function () {
            });
            const pageModePromise = pdfDocument.getPageMode()["catch"](function () {
            });
            const openActionDestPromise = pdfDocument.getOpenActionDestination()["catch"](function () {
            });
            this.toolbar.setPagesCount(pdfDocument.numPages, false);
            this.secondaryToolbar.setPagesCount(pdfDocument.numPages);
            const store = this.store = new _view_history.ViewHistory(pdfDocument.fingerprint);
            let baseDocumentUrl;
            baseDocumentUrl = null;
            this.pdfLinkService.setDocument(pdfDocument, baseDocumentUrl);
            this.pdfDocumentProperties.setDocument(pdfDocument, this.url);
            const pdfViewer = this.pdfViewer;
            pdfViewer.setDocument(pdfDocument);
            var firstPagePromise = pdfViewer.firstPagePromise;
            const pagesPromise = pdfViewer.pagesPromise;
            const onePageRendered = pdfViewer.onePageRendered;
            const pdfThumbnailViewer = this.pdfThumbnailViewer;
            pdfThumbnailViewer.setDocument(pdfDocument);
            firstPagePromise.then(function (pdfPage) {
              _this5.loadingBar.setWidth(_this5.appConfig.viewerContainer);

              const storePromise = store.getMultiple({
                page: null,
                zoom: _ui_utils.DEFAULT_SCALE_VALUE,
                scrollLeft: '0',
                scrollTop: '0',
                rotation: null,
                sidebarView: _pdf_sidebar.SidebarView.UNKNOWN,
                scrollMode: _ui_utils.ScrollMode.UNKNOWN,
                spreadMode: _ui_utils.SpreadMode.UNKNOWN
              })["catch"](function () {
              });
              Promise.all([_ui_utils.animationStarted, storePromise, pageLayoutPromise, pageModePromise, openActionDestPromise]).then(
                  /*#__PURE__*/
                  function () {
                    const _ref3 = _asyncToGenerator(
                        /*#__PURE__*/
                        _regenerator["default"].mark(function _callee8(_ref2) {
                          let _ref4, timeStamp, _ref4$, values, pageLayout, pageMode, openActionDest, viewOnLoad,
                              initialBookmark,
                              zoom,
                              hash, rotation, sidebarView, scrollMode, spreadMode;

                          return _regenerator["default"].wrap(function _callee8$(_context8) {
                            while (1) {
                              switch (_context8.prev = _context8.next) {
                                case 0:
                                  _ref4 = _slicedToArray(_ref2, 5), timeStamp = _ref4[0], _ref4$ = _ref4[1], values = _ref4$ === void 0 ? {} : _ref4$, pageLayout = _ref4[2], pageMode = _ref4[3], openActionDest = _ref4[4];
                                  viewOnLoad = _app_options.AppOptions.get('viewOnLoad');

                                  _this5._initializePdfHistory({
                                    fingerprint: pdfDocument.fingerprint,
                                    viewOnLoad: viewOnLoad,
                                    initialDest: openActionDest
                                  });

                                  initialBookmark = _this5.initialBookmark;
                                  zoom = _app_options.AppOptions.get('defaultZoomValue');
                                  hash = zoom ? "zoom=".concat(zoom) : null;
                                  rotation = null;
                                  sidebarView = _app_options.AppOptions.get('sidebarViewOnLoad');
                                  scrollMode = _app_options.AppOptions.get('scrollModeOnLoad');
                                  spreadMode = _app_options.AppOptions.get('spreadModeOnLoad');

                                  if (values.page && viewOnLoad !== ViewOnLoad.INITIAL) {
                                    hash = "page=".concat(values.page, "&zoom=").concat(zoom || values.zoom, ",") + "".concat(values.scrollLeft, ",").concat(values.scrollTop);
                                    rotation = parseInt(values.rotation, 10);

                                    if (sidebarView === _pdf_sidebar.SidebarView.UNKNOWN) {
                                      sidebarView = values.sidebarView | 0;
                                    }

                                    if (scrollMode === _ui_utils.ScrollMode.UNKNOWN) {
                                      scrollMode = values.scrollMode | 0;
                                    }

                                    if (spreadMode === _ui_utils.SpreadMode.UNKNOWN) {
                                      spreadMode = values.spreadMode | 0;
                                    }
                                  }

                                  if (pageMode && sidebarView === _pdf_sidebar.SidebarView.UNKNOWN) {
                                    sidebarView = apiPageModeToSidebarView(pageMode);
                                  }

                                  if (pageLayout && spreadMode === _ui_utils.SpreadMode.UNKNOWN) {
                                    spreadMode = apiPageLayoutToSpreadMode(pageLayout);
                                  }

                                  _this5.setInitialView(hash, {
                                    rotation: rotation,
                                    sidebarView: sidebarView,
                                    scrollMode: scrollMode,
                                    spreadMode: spreadMode
                                  });

                                  _this5.eventBus.dispatch('documentinit', {
                                    source: _this5
                                  });

                                  if (!_this5.isViewerEmbedded) {
                                    pdfViewer.focus();
                                  }

                                  _context8.next = 18;
                                  return Promise.race([pagesPromise, new Promise(function (resolve) {
                                    setTimeout(resolve, FORCE_PAGES_LOADED_TIMEOUT);
                                  })]);

                                case 18:
                                  if (!(!initialBookmark && !hash)) {
                                    _context8.next = 20;
                                    break;
                                  }

                                  return _context8.abrupt("return");

                                case 20:
                                  if (!pdfViewer.hasEqualPageSizes) {
                                    _context8.next = 22;
                                    break;
                                  }

                                  return _context8.abrupt("return");

                                case 22:
                                  _this5.initialBookmark = initialBookmark;
                                  pdfViewer.currentScaleValue = pdfViewer.currentScaleValue;

                                  _this5.setInitialView(hash);

                                case 25:
                                case "end":
                                  return _context8.stop();
                              }
                            }
                          }, _callee8);
                        }));

                    return function (_x4) {
                      return _ref3.apply(this, arguments);
                    };
                  }())["catch"](function () {
                _this5.setInitialView();
              }).then(function () {
                pdfViewer.update();
              });
            });
            pdfDocument.getPageLabels().then(function (labels) {
              if (!labels || _app_options.AppOptions.get('disablePageLabels')) {
                return;
              }

              let i = 0;
              const numLabels = labels.length;

              if (numLabels !== _this5.pagesCount) {
                console.error('The number of Page Labels does not match ' + 'the number of pages in the document.');
                return;
              }

              while (i < numLabels && labels[i] === (i + 1).toString()) {
                i++;
              }

              if (i === numLabels) {
                return;
              }

              pdfViewer.setPageLabels(labels);
              pdfThumbnailViewer.setPageLabels(labels);

              _this5.toolbar.setPagesCount(pdfDocument.numPages, true);

              _this5.toolbar.setPageNumber(pdfViewer.currentPageNumber, pdfViewer.currentPageLabel);
            });
            pagesPromise.then(function () {
              if (!_this5.supportsPrinting) {
                return;
              }

              pdfDocument.getJavaScript().then(function (javaScript) {
                if (!javaScript) {
                  return;
                }

                javaScript.some(function (js) {
                  if (!js) {
                    return false;
                  }

                  console.warn('Warning: JavaScript is not supported');

                  _this5.fallback(_pdfjsLib.UNSUPPORTED_FEATURES.javaScript);

                  return true;
                });
                const regex = /\bprint\s*\(/;

                let i = 0;
                const ii = javaScript.length;
                for (; i < ii; i++) {
                  var js = javaScript[i];

                  if (js && regex.test(js)) {
                    setTimeout(function () {
                      window.print();
                    });
                    return;
                  }
                }
              });
            });
            onePageRendered.then(function () {
              pdfDocument.getOutline().then(function (outline) {
                _this5.pdfOutlineViewer.render({
                  outline: outline
                });
              });
              pdfDocument.getAttachments().then(function (attachments) {
                _this5.pdfAttachmentViewer.render({
                  attachments: attachments
                });
              });
            });
            pdfDocument.getMetadata().then(function (_ref5) {
              const info = _ref5.info,
                  metadata = _ref5.metadata,
                  contentDispositionFilename = _ref5.contentDispositionFilename;
              _this5.documentInfo = info;
              _this5.metadata = metadata;
              _this5.contentDispositionFilename = contentDispositionFilename;
              console.log('PDF ' + pdfDocument.fingerprint + ' [' + info.PDFFormatVersion + ' ' + (info.Producer || '-').trim() + ' / ' + (info.Creator || '-').trim() + ']' + ' (PDF.js: ' + (_pdfjsLib.version || '-') + (_app_options.AppOptions.get('enableWebGL') ? ' [WebGL]' : '') + ')');
              let pdfTitle;

              if (metadata && metadata.has('dc:title')) {
                const title = metadata.get('dc:title');

                if (title !== 'Untitled') {
                  pdfTitle = title;
                }
              }

              if (!pdfTitle && info && info['Title']) {
                pdfTitle = info['Title'];
              }

              if (pdfTitle) {
                _this5.setTitle("".concat(pdfTitle, " - ").concat(contentDispositionFilename || document.title));
              } else if (contentDispositionFilename) {
                _this5.setTitle(contentDispositionFilename);
              }

              if (info.IsAcroFormPresent) {
                console.warn('Warning: AcroForm/XFA is not supported');

                _this5.fallback(_pdfjsLib.UNSUPPORTED_FEATURES.forms);
              }
            });
          },
          _initializePdfHistory: function _initializePdfHistory(_ref6) {
            const fingerprint = _ref6.fingerprint,
                viewOnLoad = _ref6.viewOnLoad,
                _ref6$initialDest = _ref6.initialDest,
                initialDest = _ref6$initialDest === void 0 ? null : _ref6$initialDest;

            if (_app_options.AppOptions.get('disableHistory') || this.isViewerEmbedded) {
              return;
            }

            this.pdfHistory.initialize({
              fingerprint: fingerprint,
              resetHistory: viewOnLoad === ViewOnLoad.INITIAL,
              updateUrl: _app_options.AppOptions.get('historyUpdateUrl')
            });

            if (this.pdfHistory.initialBookmark) {
              this.initialBookmark = this.pdfHistory.initialBookmark;
              this.initialRotation = this.pdfHistory.initialRotation;
            }

            if (initialDest && !this.initialBookmark && viewOnLoad === ViewOnLoad.UNKNOWN) {
              this.initialBookmark = JSON.stringify(initialDest);
              this.pdfHistory.push({
                explicitDest: initialDest,
                pageNumber: null
              });
            }
          },
          setInitialView: function setInitialView(storedHash) {
            const _this6 = this;

            const _ref7 = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
                rotation = _ref7.rotation,
                sidebarView = _ref7.sidebarView,
                scrollMode = _ref7.scrollMode,
                spreadMode = _ref7.spreadMode;

            const setRotation = function setRotation(angle) {
              if ((0, _ui_utils.isValidRotation)(angle)) {
                _this6.pdfViewer.pagesRotation = angle;
              }
            };

            const setViewerModes = function setViewerModes(scroll, spread) {
              if ((0, _ui_utils.isValidScrollMode)(scroll)) {
                _this6.pdfViewer.scrollMode = scroll;
              }

              if ((0, _ui_utils.isValidSpreadMode)(spread)) {
                _this6.pdfViewer.spreadMode = spread;
              }
            };

            this.isInitialViewSet = true;
            this.pdfSidebar.setInitialView(sidebarView);
            setViewerModes(scrollMode, spreadMode);

            if (this.initialBookmark) {
              setRotation(this.initialRotation);
              delete this.initialRotation;
              this.pdfLinkService.setHash(this.initialBookmark);
              this.initialBookmark = null;
            } else if (storedHash) {
              setRotation(rotation);
              this.pdfLinkService.setHash(storedHash);
            }

            this.toolbar.setPageNumber(this.pdfViewer.currentPageNumber, this.pdfViewer.currentPageLabel);
            this.secondaryToolbar.setPageNumber(this.pdfViewer.currentPageNumber);

            if (!this.pdfViewer.currentScaleValue) {
              this.pdfViewer.currentScaleValue = _ui_utils.DEFAULT_SCALE_VALUE;
            }
          },
          cleanup: function cleanup() {
            if (!this.pdfDocument) {
              return;
            }

            this.pdfViewer.cleanup();
            this.pdfThumbnailViewer.cleanup();

            if (this.pdfViewer.renderer !== _ui_utils.RendererType.SVG) {
              this.pdfDocument.cleanup();
            }
          },
          forceRendering: function forceRendering() {
            this.pdfRenderingQueue.printing = this.printing;
            this.pdfRenderingQueue.isThumbnailViewEnabled = this.pdfSidebar.isThumbnailViewVisible;
            this.pdfRenderingQueue.renderHighestPriority();
          },
          beforePrint: function beforePrint() {
            const _this7 = this;

            if (this.printService) {
              return;
            }

            if (!this.supportsPrinting) {
              this.l10n.get('printing_not_supported', null, 'Warning: Printing is not fully supported by ' + 'this browser.').then(function (printMessage) {
                _this7.error(printMessage);
              });
              return;
            }

            if (!this.pdfViewer.pageViewsReady) {
              this.l10n.get('printing_not_ready', null, 'Warning: The PDF is not fully loaded for printing.').then(function (notReadyMessage) {
                window.alert(notReadyMessage);
              });
              return;
            }

            const pagesOverview = this.pdfViewer.getPagesOverview();
            const printContainer = this.appConfig.printContainer;
            const printService = PDFPrintServiceFactory.instance.createPrintService(this.pdfDocument, pagesOverview, printContainer, this.l10n);
            this.printService = printService;
            this.forceRendering();
            printService.layout();
          },
          afterPrint: function afterPrint() {
            if (this.printService) {
              this.printService.destroy();
              this.printService = null;
            }

            this.forceRendering();
          },
          rotatePages: function rotatePages(delta) {
            if (!this.pdfDocument) {
              return;
            }

            const newRotation = (this.pdfViewer.pagesRotation + 360 + delta) % 360;
            this.pdfViewer.pagesRotation = newRotation;
          },
          requestPresentationMode: function requestPresentationMode() {
            if (!this.pdfPresentationMode) {
              return;
            }

            this.pdfPresentationMode.request();
          },
          bindEvents: function bindEvents() {
            const eventBus = this.eventBus,
                _boundEvents = this._boundEvents;
            _boundEvents.beforePrint = this.beforePrint.bind(this);
            _boundEvents.afterPrint = this.afterPrint.bind(this);
            eventBus.on('resize', webViewerResize);
            eventBus.on('hashchange', webViewerHashchange);
            eventBus.on('beforeprint', _boundEvents.beforePrint);
            eventBus.on('afterprint', _boundEvents.afterPrint);
            eventBus.on('pagerendered', webViewerPageRendered);
            eventBus.on('textlayerrendered', webViewerTextLayerRendered);
            eventBus.on('updateviewarea', webViewerUpdateViewarea);
            eventBus.on('pagechanging', webViewerPageChanging);
            eventBus.on('scalechanging', webViewerScaleChanging);
            eventBus.on('rotationchanging', webViewerRotationChanging);
            eventBus.on('sidebarviewchanged', webViewerSidebarViewChanged);
            eventBus.on('pagemode', webViewerPageMode);
            eventBus.on('namedaction', webViewerNamedAction);
            eventBus.on('presentationmodechanged', webViewerPresentationModeChanged);
            eventBus.on('presentationmode', webViewerPresentationMode);
            eventBus.on('openfile', webViewerOpenFile);
            eventBus.on('print', webViewerPrint);
            eventBus.on('download', webViewerDownload);
            eventBus.on('firstpage', webViewerFirstPage);
            eventBus.on('lastpage', webViewerLastPage);
            eventBus.on('nextpage', webViewerNextPage);
            eventBus.on('previouspage', webViewerPreviousPage);
            eventBus.on('zoomin', webViewerZoomIn);
            eventBus.on('zoomout', webViewerZoomOut);
            eventBus.on('zoomreset', webViewerZoomReset);
            eventBus.on('pagenumberchanged', webViewerPageNumberChanged);
            eventBus.on('scalechanged', webViewerScaleChanged);
            eventBus.on('rotatecw', webViewerRotateCw);
            eventBus.on('rotateccw', webViewerRotateCcw);
            eventBus.on('switchscrollmode', webViewerSwitchScrollMode);
            eventBus.on('scrollmodechanged', webViewerScrollModeChanged);
            eventBus.on('switchspreadmode', webViewerSwitchSpreadMode);
            eventBus.on('spreadmodechanged', webViewerSpreadModeChanged);
            eventBus.on('documentproperties', webViewerDocumentProperties);
            eventBus.on('find', webViewerFind);
            eventBus.on('findfromurlhash', webViewerFindFromUrlHash);
            eventBus.on('updatefindmatchescount', webViewerUpdateFindMatchesCount);
            eventBus.on('updatefindcontrolstate', webViewerUpdateFindControlState);
            eventBus.on('fileinputchange', webViewerFileInputChange);
          },
          bindWindowEvents: function bindWindowEvents() {
            const eventBus = this.eventBus,
                _boundEvents = this._boundEvents;

            _boundEvents.windowResize = function () {
              eventBus.dispatch('resize', {
                source: window
              });
            };

            _boundEvents.windowHashChange = function () {
              eventBus.dispatch('hashchange', {
                source: window,
                hash: document.location.hash.substring(1)
              });
            };

            _boundEvents.windowBeforePrint = function () {
              eventBus.dispatch('beforeprint', {
                source: window
              });
            };

            _boundEvents.windowAfterPrint = function () {
              eventBus.dispatch('afterprint', {
                source: window
              });
            };

            window.addEventListener('visibilitychange', webViewerVisibilityChange);
            window.addEventListener('wheel', webViewerWheel, {
              passive: false
            });
            window.addEventListener('click', webViewerClick);
            window.addEventListener('keydown', webViewerKeyDown);
            window.addEventListener('resize', _boundEvents.windowResize);
            window.addEventListener('hashchange', _boundEvents.windowHashChange);
            window.addEventListener('beforeprint', _boundEvents.windowBeforePrint);
            window.addEventListener('afterprint', _boundEvents.windowAfterPrint);
          },
          unbindEvents: function unbindEvents() {
            const eventBus = this.eventBus,
                _boundEvents = this._boundEvents;
            eventBus.off('resize', webViewerResize);
            eventBus.off('hashchange', webViewerHashchange);
            eventBus.off('beforeprint', _boundEvents.beforePrint);
            eventBus.off('afterprint', _boundEvents.afterPrint);
            eventBus.off('pagerendered', webViewerPageRendered);
            eventBus.off('textlayerrendered', webViewerTextLayerRendered);
            eventBus.off('updateviewarea', webViewerUpdateViewarea);
            eventBus.off('pagechanging', webViewerPageChanging);
            eventBus.off('scalechanging', webViewerScaleChanging);
            eventBus.off('rotationchanging', webViewerRotationChanging);
            eventBus.off('sidebarviewchanged', webViewerSidebarViewChanged);
            eventBus.off('pagemode', webViewerPageMode);
            eventBus.off('namedaction', webViewerNamedAction);
            eventBus.off('presentationmodechanged', webViewerPresentationModeChanged);
            eventBus.off('presentationmode', webViewerPresentationMode);
            eventBus.off('openfile', webViewerOpenFile);
            eventBus.off('print', webViewerPrint);
            eventBus.off('download', webViewerDownload);
            eventBus.off('firstpage', webViewerFirstPage);
            eventBus.off('lastpage', webViewerLastPage);
            eventBus.off('nextpage', webViewerNextPage);
            eventBus.off('previouspage', webViewerPreviousPage);
            eventBus.off('zoomin', webViewerZoomIn);
            eventBus.off('zoomout', webViewerZoomOut);
            eventBus.off('zoomreset', webViewerZoomReset);
            eventBus.off('pagenumberchanged', webViewerPageNumberChanged);
            eventBus.off('scalechanged', webViewerScaleChanged);
            eventBus.off('rotatecw', webViewerRotateCw);
            eventBus.off('rotateccw', webViewerRotateCcw);
            eventBus.off('switchscrollmode', webViewerSwitchScrollMode);
            eventBus.off('scrollmodechanged', webViewerScrollModeChanged);
            eventBus.off('switchspreadmode', webViewerSwitchSpreadMode);
            eventBus.off('spreadmodechanged', webViewerSpreadModeChanged);
            eventBus.off('documentproperties', webViewerDocumentProperties);
            eventBus.off('find', webViewerFind);
            eventBus.off('findfromurlhash', webViewerFindFromUrlHash);
            eventBus.off('updatefindmatchescount', webViewerUpdateFindMatchesCount);
            eventBus.off('updatefindcontrolstate', webViewerUpdateFindControlState);
            eventBus.off('fileinputchange', webViewerFileInputChange);
            _boundEvents.beforePrint = null;
            _boundEvents.afterPrint = null;
          },
          unbindWindowEvents: function unbindWindowEvents() {
            const _boundEvents = this._boundEvents;
            window.removeEventListener('visibilitychange', webViewerVisibilityChange);
            window.removeEventListener('wheel', webViewerWheel);
            window.removeEventListener('click', webViewerClick);
            window.removeEventListener('keydown', webViewerKeyDown);
            window.removeEventListener('resize', _boundEvents.windowResize);
            window.removeEventListener('hashchange', _boundEvents.windowHashChange);
            window.removeEventListener('beforeprint', _boundEvents.windowBeforePrint);
            window.removeEventListener('afterprint', _boundEvents.windowAfterPrint);
            _boundEvents.windowResize = null;
            _boundEvents.windowHashChange = null;
            _boundEvents.windowBeforePrint = null;
            _boundEvents.windowAfterPrint = null;
          }
        };
        exports.PDFViewerApplication = PDFViewerApplication;
        let validateFileURL;
        {
  const HOSTED_VIEWER_ORIGINS = ['null', 'http://mozilla.github.io', 'https://mozilla.github.io'];

  validateFileURL = function validateFileURL(file) {
    if (file === undefined) {
      return;
    }

    try {
      const viewerOrigin = new URL(window.location.href).origin || 'null';

      if (HOSTED_VIEWER_ORIGINS.includes(viewerOrigin)) {
        return;
      }

      const _ref8 = new URL(file, window.location.href),
          origin = _ref8.origin,
          protocol = _ref8.protocol;

      if (origin !== viewerOrigin && protocol !== 'blob:') {
        throw new Error('file origin does not match viewer\'s');
      }
    } catch (ex) {
      const message = ex && ex.message;
      PDFViewerApplication.l10n.get('loading_error', null, 'An error occurred while loading the PDF.').then(function (loadingErrorMessage) {
        PDFViewerApplication.error(loadingErrorMessage, {
          message: message
        });
      });
      throw ex;
    }
  };
}

function loadFakeWorker() {
  if (!_pdfjsLib.GlobalWorkerOptions.workerSrc) {
    _pdfjsLib.GlobalWorkerOptions.workerSrc = _app_options.AppOptions.get('workerSrc');
  }

  return (0, _pdfjsLib.loadScript)(_pdfjsLib.PDFWorker.getWorkerSrc());
}

function loadAndEnablePDFBug(enabledTabs) {
  const appConfig = PDFViewerApplication.appConfig;
  return (0, _pdfjsLib.loadScript)(appConfig.debuggerScriptPath).then(function () {
    PDFBug.enable(enabledTabs);
    PDFBug.init({
      OPS: _pdfjsLib.OPS,
      createObjectURL: _pdfjsLib.createObjectURL
    }, appConfig.mainContainer);
  });
}

function webViewerInitialized() {
  const appConfig = PDFViewerApplication.appConfig;
  let file;
  const queryString = document.location.search.substring(1);
  const params = _ui_utils.parseQueryString(queryString);
  file = 'file' in params ? params.file : _app_options.AppOptions.get('defaultUrl');
  validateFileURL(file);
  const fileInput = document.createElement('input');
  fileInput.id = appConfig.openFileInputName;
  fileInput.className = 'fileInput';
  fileInput.setAttribute('type', 'file');
  fileInput.oncontextmenu = _ui_utils.noContextMenuHandler;
  document.body.appendChild(fileInput);

  if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
    appConfig.toolbar.openFile.setAttribute('hidden', 'true');
    appConfig.secondaryToolbar.openFileButton.setAttribute('hidden', 'true');
  } else {
    fileInput.value = null;
  }

  fileInput.addEventListener('change', function (evt) {
    const files = evt.target.files;

    if (!files || files.length === 0) {
      return;
    }

    PDFViewerApplication.eventBus.dispatch('fileinputchange', {
      source: this,
      fileInput: evt.target
    });
  });
  appConfig.mainContainer.addEventListener('dragover', function (evt) {
    evt.preventDefault();
    evt.dataTransfer.dropEffect = 'move';
  });
  appConfig.mainContainer.addEventListener('drop', function (evt) {
    evt.preventDefault();
    const files = evt.dataTransfer.files;

    if (!files || files.length === 0) {
      return;
    }

    PDFViewerApplication.eventBus.dispatch('fileinputchange', {
      source: this,
      fileInput: evt.dataTransfer
    });
  });

  if (!PDFViewerApplication.supportsPrinting) {
    appConfig.toolbar.print.classList.add('hidden');
    appConfig.secondaryToolbar.printButton.classList.add('hidden');
  }

  if (!PDFViewerApplication.supportsFullscreen) {
    appConfig.toolbar.presentationModeButton.classList.add('hidden');
    appConfig.secondaryToolbar.presentationModeButton.classList.add('hidden');
  }

  if (PDFViewerApplication.supportsIntegratedFind) {
    appConfig.toolbar.viewFind.classList.add('hidden');
  }

  appConfig.mainContainer.addEventListener('transitionend', function (evt) {
    if (evt.target === this) {
      PDFViewerApplication.eventBus.dispatch('resize', {
        source: this
      });
    }
  }, true);

  try {
    webViewerOpenFileViaURL(file);
  } catch (reason) {
    PDFViewerApplication.l10n.get('loading_error', null, 'An error occurred while loading the PDF.').then(function (msg) {
      PDFViewerApplication.error(msg, reason);
    });
  }
}

var webViewerOpenFileViaURL;
{
  webViewerOpenFileViaURL = function webViewerOpenFileViaURL(file) {
    if (file && file.lastIndexOf('file:', 0) === 0) {
      PDFViewerApplication.setTitleUsingUrl(file);
      const xhr = new XMLHttpRequest();

      xhr.onload = function () {
        PDFViewerApplication.open(new Uint8Array(xhr.response));
      };

      xhr.open('GET', file);
      xhr.responseType = 'arraybuffer';
      xhr.send();
      return;
    }

    if (file) {
      PDFViewerApplication.open(file);
    }
  };
}

function webViewerPageRendered(evt) {
  const pageNumber = evt.pageNumber;
  const pageIndex = pageNumber - 1;
  const pageView = PDFViewerApplication.pdfViewer.getPageView(pageIndex);

  if (pageNumber === PDFViewerApplication.page) {
    PDFViewerApplication.toolbar.updateLoadingIndicatorState(false);
  }

  if (!pageView) {
    return;
  }

  if (PDFViewerApplication.pdfSidebar.isThumbnailViewVisible) {
    const thumbnailView = PDFViewerApplication.pdfThumbnailViewer.getThumbnail(pageIndex);
    thumbnailView.setImage(pageView);
  }

  if (typeof Stats !== 'undefined' && Stats.enabled && pageView.stats) {
    Stats.add(pageNumber, pageView.stats);
  }

  if (pageView.error) {
    PDFViewerApplication.l10n.get('rendering_error', null, 'An error occurred while rendering the page.').then(function (msg) {
      PDFViewerApplication.error(msg, pageView.error);
    });
  }
}

function webViewerTextLayerRendered(evt) {}

function webViewerPageMode(evt) {
  const mode = evt.mode;
  let view;

  switch (mode) {
    case 'thumbs':
      view = _pdf_sidebar.SidebarView.THUMBS;
      break;

    case 'bookmarks':
    case 'outline':
      view = _pdf_sidebar.SidebarView.OUTLINE;
      break;

    case 'attachments':
      view = _pdf_sidebar.SidebarView.ATTACHMENTS;
      break;

    case 'none':
      view = _pdf_sidebar.SidebarView.NONE;
      break;

    default:
      console.error('Invalid "pagemode" hash parameter: ' + mode);
      return;
  }

  PDFViewerApplication.pdfSidebar.switchView(view, true);
}

function webViewerNamedAction(evt) {
  const action = evt.action;

  switch (action) {
    case 'GoToPage':
      PDFViewerApplication.appConfig.toolbar.pageNumber.select();
      break;

    case 'Find':
      if (!PDFViewerApplication.supportsIntegratedFind) {
        PDFViewerApplication.findBar.toggle();
      }

      break;
  }
}

function webViewerPresentationModeChanged(evt) {
  const active = evt.active,
      switchInProgress = evt.switchInProgress;
  PDFViewerApplication.pdfViewer.presentationModeState = switchInProgress ? _ui_utils.PresentationModeState.CHANGING : active ? _ui_utils.PresentationModeState.FULLSCREEN : _ui_utils.PresentationModeState.NORMAL;
}

function webViewerSidebarViewChanged(evt) {
  PDFViewerApplication.pdfRenderingQueue.isThumbnailViewEnabled = PDFViewerApplication.pdfSidebar.isThumbnailViewVisible;
  const store = PDFViewerApplication.store;

  if (store && PDFViewerApplication.isInitialViewSet) {
    store.set('sidebarView', evt.view)["catch"](function () {});
  }
}

function webViewerUpdateViewarea(evt) {
  const location = evt.location,
      store = PDFViewerApplication.store;

  if (store && PDFViewerApplication.isInitialViewSet) {
    store.setMultiple({
      'page': location.pageNumber,
      'zoom': location.scale,
      'scrollLeft': location.left,
      'scrollTop': location.top,
      'rotation': location.rotation
    })["catch"](function () {});
  }

  const href = PDFViewerApplication.pdfLinkService.getAnchorUrl(location.pdfOpenParams);
  PDFViewerApplication.appConfig.toolbar.viewBookmark.href = href;
  PDFViewerApplication.appConfig.secondaryToolbar.viewBookmarkButton.href = href;
  const currentPage = PDFViewerApplication.pdfViewer.getPageView(PDFViewerApplication.page - 1);
  const loading = currentPage.renderingState !== _pdf_rendering_queue.RenderingStates.FINISHED;
  PDFViewerApplication.toolbar.updateLoadingIndicatorState(loading);
}

function webViewerScrollModeChanged(evt) {
  const store = PDFViewerApplication.store;

  if (store && PDFViewerApplication.isInitialViewSet) {
    store.set('scrollMode', evt.mode)["catch"](function () {});
  }
}

function webViewerSpreadModeChanged(evt) {
  const store = PDFViewerApplication.store;

  if (store && PDFViewerApplication.isInitialViewSet) {
    store.set('spreadMode', evt.mode)["catch"](function () {});
  }
}

function webViewerResize() {
  const pdfDocument = PDFViewerApplication.pdfDocument,
      pdfViewer = PDFViewerApplication.pdfViewer;

  if (!pdfDocument) {
    return;
  }

  const currentScaleValue = pdfViewer.currentScaleValue;

  if (currentScaleValue === 'auto' || currentScaleValue === 'page-fit' || currentScaleValue === 'page-width') {
    pdfViewer.currentScaleValue = currentScaleValue;
  }

  pdfViewer.update();
}

function webViewerHashchange(evt) {
  const hash = evt.hash;

  if (!hash) {
    return;
  }

  if (!PDFViewerApplication.isInitialViewSet) {
    PDFViewerApplication.initialBookmark = hash;
  } else if (!PDFViewerApplication.pdfHistory.popStateInProgress) {
    PDFViewerApplication.pdfLinkService.setHash(hash);
  }
}

var webViewerFileInputChange;
{
  webViewerFileInputChange = function webViewerFileInputChange(evt) {
    console.log("a file input change happened");
    if (PDFViewerApplication.pdfViewer && PDFViewerApplication.pdfViewer.isInPresentationMode) {
      console.log("i am not gonna handle this change cuz of presentation mode");
      return;
    }

    const file = evt.fileInput.files[0];

    if (URL.createObjectURL && !_app_options.AppOptions.get('disableCreateObjectURL')) {
      let url = URL.createObjectURL(file);

      if (file.name) {
        url = {
          url: url,
          originalUrl: file.name
        };
      }
      PDFViewerApplication.open(url);
    } else {
      PDFViewerApplication.setTitleUsingUrl(file.name);
      const fileReader = new FileReader();

      fileReader.onload = function webViewerChangeFileReaderOnload(evt) {
        const buffer = evt.target.result;
        PDFViewerApplication.open(new Uint8Array(buffer));
      };
        console.log("reading as buffer");
      fileReader.readAsArrayBuffer(file);
    }

    const appConfig = PDFViewerApplication.appConfig;
    appConfig.toolbar.viewBookmark.setAttribute('hidden', 'true');
    appConfig.secondaryToolbar.viewBookmarkButton.setAttribute('hidden', 'true');
    appConfig.toolbar.download.setAttribute('hidden', 'true');
    appConfig.secondaryToolbar.downloadButton.setAttribute('hidden', 'true');
    console.log("i finished handling event");
  };
}

function webViewerPresentationMode() {
  PDFViewerApplication.requestPresentationMode();
}

function webViewerOpenFile() {
  const openFileInputName = PDFViewerApplication.appConfig.openFileInputName;
  document.getElementById(openFileInputName).click();
}

function webViewerPrint() {
  window.print();
}

function webViewerDownload() {
  PDFViewerApplication.download();
}

function webViewerFirstPage() {
  if (PDFViewerApplication.pdfDocument) {
    PDFViewerApplication.page = 1;
  }
}

function webViewerLastPage() {
  if (PDFViewerApplication.pdfDocument) {
    PDFViewerApplication.page = PDFViewerApplication.pagesCount;
  }
}

function webViewerNextPage() {
  PDFViewerApplication.page++;
}

function webViewerPreviousPage() {
  PDFViewerApplication.page--;
}

function webViewerZoomIn() {
  PDFViewerApplication.zoomIn();
}

function webViewerZoomOut() {
  PDFViewerApplication.zoomOut();
}

function webViewerZoomReset() {
  PDFViewerApplication.zoomReset();
}

function webViewerPageNumberChanged(evt) {
  const pdfViewer = PDFViewerApplication.pdfViewer;

  if (evt.value !== '') {
    pdfViewer.currentPageLabel = evt.value;
  }

  if (evt.value !== pdfViewer.currentPageNumber.toString() && evt.value !== pdfViewer.currentPageLabel) {
    PDFViewerApplication.toolbar.setPageNumber(pdfViewer.currentPageNumber, pdfViewer.currentPageLabel);
  }
}

function webViewerScaleChanged(evt) {
  PDFViewerApplication.pdfViewer.currentScaleValue = evt.value;
}

function webViewerRotateCw() {
  PDFViewerApplication.rotatePages(90);
}

function webViewerRotateCcw() {
  PDFViewerApplication.rotatePages(-90);
}

function webViewerSwitchScrollMode(evt) {
  PDFViewerApplication.pdfViewer.scrollMode = evt.mode;
}

function webViewerSwitchSpreadMode(evt) {
  PDFViewerApplication.pdfViewer.spreadMode = evt.mode;
}

function webViewerDocumentProperties() {
  PDFViewerApplication.pdfDocumentProperties.open();
}

function webViewerFind(evt) {
  PDFViewerApplication.findController.executeCommand('find' + evt.type, {
    query: evt.query,
    phraseSearch: evt.phraseSearch,
    caseSensitive: evt.caseSensitive,
    entireWord: evt.entireWord,
    highlightAll: evt.highlightAll,
    findPrevious: evt.findPrevious
  });
}

function webViewerFindFromUrlHash(evt) {
  PDFViewerApplication.findController.executeCommand('find', {
    query: evt.query,
    phraseSearch: evt.phraseSearch,
    caseSensitive: false,
    entireWord: false,
    highlightAll: true,
    findPrevious: false
  });
}

function webViewerUpdateFindMatchesCount(_ref9) {
  const matchesCount = _ref9.matchesCount;

  if (PDFViewerApplication.supportsIntegratedFind) {
    PDFViewerApplication.externalServices.updateFindMatchesCount(matchesCount);
  } else {
    PDFViewerApplication.findBar.updateResultsCount(matchesCount);
  }
}

function webViewerUpdateFindControlState(_ref10) {
  const state = _ref10.state,
      previous = _ref10.previous,
      matchesCount = _ref10.matchesCount;

  if (PDFViewerApplication.supportsIntegratedFind) {
    PDFViewerApplication.externalServices.updateFindControlState({
      result: state,
      findPrevious: previous,
      matchesCount: matchesCount
    });
  } else {
    PDFViewerApplication.findBar.updateUIState(state, previous, matchesCount);
  }
}

function webViewerScaleChanging(evt) {
  PDFViewerApplication.toolbar.setPageScale(evt.presetValue, evt.scale);
  PDFViewerApplication.pdfViewer.update();
}

function webViewerRotationChanging(evt) {
  PDFViewerApplication.pdfThumbnailViewer.pagesRotation = evt.pagesRotation;
  PDFViewerApplication.forceRendering();
  PDFViewerApplication.pdfViewer.currentPageNumber = evt.pageNumber;
}

function webViewerPageChanging(evt) {
  const page = evt.pageNumber;
  PDFViewerApplication.toolbar.setPageNumber(page, evt.pageLabel || null);
  PDFViewerApplication.secondaryToolbar.setPageNumber(page);

  if (PDFViewerApplication.pdfSidebar.isThumbnailViewVisible) {
    PDFViewerApplication.pdfThumbnailViewer.scrollThumbnailIntoView(page);
  }

  if (typeof Stats !== 'undefined' && Stats.enabled) {
    const pageView = PDFViewerApplication.pdfViewer.getPageView(page - 1);

    if (pageView && pageView.stats) {
      Stats.add(page, pageView.stats);
    }
  }
}

function webViewerVisibilityChange(evt) {
  if (document.visibilityState === 'visible') {
    setZoomDisabledTimeout();
  }
}

        let zoomDisabledTimeout = null;

        function setZoomDisabledTimeout() {
  if (zoomDisabledTimeout) {
    clearTimeout(zoomDisabledTimeout);
  }

  zoomDisabledTimeout = setTimeout(function () {
    zoomDisabledTimeout = null;
  }, WHEEL_ZOOM_DISABLED_TIMEOUT);
}

function webViewerWheel(evt) {
  const pdfViewer = PDFViewerApplication.pdfViewer,
      supportedMouseWheelZoomModifierKeys = PDFViewerApplication.supportedMouseWheelZoomModifierKeys;

  if (pdfViewer.isInPresentationMode) {
    return;
  }

  if (evt.ctrlKey && supportedMouseWheelZoomModifierKeys.ctrlKey || evt.metaKey && supportedMouseWheelZoomModifierKeys.metaKey) {
    evt.preventDefault();

    if (zoomDisabledTimeout || document.visibilityState === 'hidden') {
      return;
    }

    const previousScale = pdfViewer.currentScale;
    const delta = (0, _ui_utils.normalizeWheelEventDelta)(evt);
    const MOUSE_WHEEL_DELTA_PER_PAGE_SCALE = 3.0;
    const ticks = delta * MOUSE_WHEEL_DELTA_PER_PAGE_SCALE;

    if (ticks < 0) {
      PDFViewerApplication.zoomOut(-ticks);
    } else {
      PDFViewerApplication.zoomIn(ticks);
    }

    const currentScale = pdfViewer.currentScale;

    if (previousScale !== currentScale) {
      const scaleCorrectionFactor = currentScale / previousScale - 1;
      const rect = pdfViewer.container.getBoundingClientRect();
      const dx = evt.clientX - rect.left;
      const dy = evt.clientY - rect.top;
      pdfViewer.container.scrollLeft += dx * scaleCorrectionFactor;
      pdfViewer.container.scrollTop += dy * scaleCorrectionFactor;
    }
  } else {
    setZoomDisabledTimeout();
  }
}

function webViewerClick(evt) {
  if (!PDFViewerApplication.secondaryToolbar.isOpen) {
    return;
  }

  const appConfig = PDFViewerApplication.appConfig;

  if (PDFViewerApplication.pdfViewer.containsElement(evt.target) || appConfig.toolbar.container.contains(evt.target) && evt.target !== appConfig.secondaryToolbar.toggleButton) {
    PDFViewerApplication.secondaryToolbar.close();
  }
}

function webViewerKeyDown(evt) {
  if (PDFViewerApplication.overlayManager.active) {
    return;
  }

  let handled = false,
      ensureViewerFocused = false;
  const cmd = (evt.ctrlKey ? 1 : 0) | (evt.altKey ? 2 : 0) | (evt.shiftKey ? 4 : 0) | (evt.metaKey ? 8 : 0);
  const pdfViewer = PDFViewerApplication.pdfViewer;
  const isViewerInPresentationMode = pdfViewer && pdfViewer.isInPresentationMode;

  if (cmd === 1 || cmd === 8 || cmd === 5 || cmd === 12) {
    switch (evt.keyCode) {
      case 70:
        if (!PDFViewerApplication.supportsIntegratedFind) {
          PDFViewerApplication.findBar.open();
          handled = true;
        }

        break;

      case 71:
        if (!PDFViewerApplication.supportsIntegratedFind) {
          const findState = PDFViewerApplication.findController.state;

          if (findState) {
            PDFViewerApplication.findController.executeCommand('findagain', {
              query: findState.query,
              phraseSearch: findState.phraseSearch,
              caseSensitive: findState.caseSensitive,
              entireWord: findState.entireWord,
              highlightAll: findState.highlightAll,
              findPrevious: cmd === 5 || cmd === 12
            });
          }

          handled = true;
        }

        break;

      case 61:
      case 107:
      case 187:
      case 171:
        if (!isViewerInPresentationMode) {
          PDFViewerApplication.zoomIn();
        }

        handled = true;
        break;

      case 173:
      case 109:
      case 189:
        if (!isViewerInPresentationMode) {
          PDFViewerApplication.zoomOut();
        }

        handled = true;
        break;

      case 48:
      case 96:
        if (!isViewerInPresentationMode) {
          setTimeout(function () {
            PDFViewerApplication.zoomReset();
          });
          handled = false;
        }

        break;

      case 38:
        if (isViewerInPresentationMode || PDFViewerApplication.page > 1) {
          PDFViewerApplication.page = 1;
          handled = true;
          ensureViewerFocused = true;
        }

        break;

      case 40:
        if (isViewerInPresentationMode || PDFViewerApplication.page < PDFViewerApplication.pagesCount) {
          PDFViewerApplication.page = PDFViewerApplication.pagesCount;
          handled = true;
          ensureViewerFocused = true;
        }

        break;
    }
  }

  if (cmd === 1 || cmd === 8) {
    switch (evt.keyCode) {
      case 83:
        PDFViewerApplication.download();
        handled = true;
        break;
    }
  }

  if (cmd === 3 || cmd === 10) {
    switch (evt.keyCode) {
      case 80:
        PDFViewerApplication.requestPresentationMode();
        handled = true;
        break;

      case 71:
        PDFViewerApplication.appConfig.toolbar.pageNumber.select();
        handled = true;
        break;
    }
  }

  if (handled) {
    if (ensureViewerFocused && !isViewerInPresentationMode) {
      pdfViewer.focus();
    }

    evt.preventDefault();
    return;
  }

  const curElement = document.activeElement || document.querySelector(':focus');
  const curElementTagName = curElement && curElement.tagName.toUpperCase();

  if (curElementTagName === 'INPUT' || curElementTagName === 'TEXTAREA' || curElementTagName === 'SELECT') {
    if (evt.keyCode !== 27) {
      return;
    }
  }

  if (cmd === 0) {
    let turnPage = 0,
        turnOnlyIfPageFit = false;

    switch (evt.keyCode) {
      case 38:
      case 33:
        if (pdfViewer.isVerticalScrollbarEnabled) {
          turnOnlyIfPageFit = true;
        }

        turnPage = -1;
        break;

      case 8:
        if (!isViewerInPresentationMode) {
          turnOnlyIfPageFit = true;
        }

        turnPage = -1;
        break;

      case 37:
        if (pdfViewer.isHorizontalScrollbarEnabled) {
          turnOnlyIfPageFit = true;
        }

      case 75:
      case 80:
        turnPage = -1;
        break;

      case 27:
        if (PDFViewerApplication.secondaryToolbar.isOpen) {
          PDFViewerApplication.secondaryToolbar.close();
          handled = true;
        }

        if (!PDFViewerApplication.supportsIntegratedFind && PDFViewerApplication.findBar.opened) {
          PDFViewerApplication.findBar.close();
          handled = true;
        }

        break;

      case 40:
      case 34:
        if (pdfViewer.isVerticalScrollbarEnabled) {
          turnOnlyIfPageFit = true;
        }

        turnPage = 1;
        break;

      case 13:
      case 32:
        if (!isViewerInPresentationMode) {
          turnOnlyIfPageFit = true;
        }

        turnPage = 1;
        break;

      case 39:
        if (pdfViewer.isHorizontalScrollbarEnabled) {
          turnOnlyIfPageFit = true;
        }

      case 74:
      case 78:
        turnPage = 1;
        break;

      case 36:
        if (isViewerInPresentationMode || PDFViewerApplication.page > 1) {
          PDFViewerApplication.page = 1;
          handled = true;
          ensureViewerFocused = true;
        }

        break;

      case 35:
        if (isViewerInPresentationMode || PDFViewerApplication.page < PDFViewerApplication.pagesCount) {
          PDFViewerApplication.page = PDFViewerApplication.pagesCount;
          handled = true;
          ensureViewerFocused = true;
        }

        break;

      case 83:
        PDFViewerApplication.pdfCursorTools.switchTool(_pdf_cursor_tools.CursorTool.SELECT);
        break;

      case 72:
        PDFViewerApplication.pdfCursorTools.switchTool(_pdf_cursor_tools.CursorTool.HAND);
        break;

      case 82:
        PDFViewerApplication.rotatePages(90);
        break;

      case 115:
        PDFViewerApplication.pdfSidebar.toggle();
        break;
    }

    if (turnPage !== 0 && (!turnOnlyIfPageFit || pdfViewer.currentScaleValue === 'page-fit')) {
      if (turnPage > 0) {
        if (PDFViewerApplication.page < PDFViewerApplication.pagesCount) {
          PDFViewerApplication.page++;
        }
      } else {
        if (PDFViewerApplication.page > 1) {
          PDFViewerApplication.page--;
        }
      }

      handled = true;
    }
  }

  if (cmd === 4) {
    switch (evt.keyCode) {
      case 13:
      case 32:
        if (!isViewerInPresentationMode && pdfViewer.currentScaleValue !== 'page-fit') {
          break;
        }

        if (PDFViewerApplication.page > 1) {
          PDFViewerApplication.page--;
        }

        handled = true;
        break;

      case 82:
        PDFViewerApplication.rotatePages(-90);
        break;
    }
  }

  if (!handled && !isViewerInPresentationMode) {
    if (evt.keyCode >= 33 && evt.keyCode <= 40 || evt.keyCode === 32 && curElementTagName !== 'BUTTON') {
      ensureViewerFocused = true;
    }
  }

  if (ensureViewerFocused && !pdfViewer.containsElement(curElement)) {
    pdfViewer.focus();
  }

  if (handled) {
    evt.preventDefault();
  }
}

function apiPageLayoutToSpreadMode(layout) {
  switch (layout) {
    case 'SinglePage':
    case 'OneColumn':
      return _ui_utils.SpreadMode.NONE;

    case 'TwoColumnLeft':
    case 'TwoPageLeft':
      return _ui_utils.SpreadMode.ODD;

    case 'TwoColumnRight':
    case 'TwoPageRight':
      return _ui_utils.SpreadMode.EVEN;
  }

  return _ui_utils.SpreadMode.NONE;
}

function apiPageModeToSidebarView(mode) {
  switch (mode) {
    case 'UseNone':
      return _pdf_sidebar.SidebarView.NONE;

    case 'UseThumbs':
      return _pdf_sidebar.SidebarView.THUMBS;

    case 'UseOutlines':
      return _pdf_sidebar.SidebarView.OUTLINE;

    case 'UseAttachments':
      return _pdf_sidebar.SidebarView.ATTACHMENTS;

    case 'UseOC':
  }

  return _pdf_sidebar.SidebarView.NONE;
}

var PDFPrintServiceFactory = {
  instance: {
    supportsPrinting: false,
    createPrintService: function createPrintService() {
      throw new Error('Not implemented: createPrintService');
    }
  }
};
exports.PDFPrintServiceFactory = PDFPrintServiceFactory;

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = __webpack_require__(3);

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(module) {

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

          const runtime = function (exports) {
            "use strict";

            const Op = Object.prototype;
            const hasOwn = Op.hasOwnProperty;
            let undefined;
            const $Symbol = typeof Symbol === "function" ? Symbol : {};
            const iteratorSymbol = $Symbol.iterator || "@@iterator";
            const asyncIteratorSymbol = $Symbol.asyncIterator || "@@asyncIterator";
            const toStringTagSymbol = $Symbol.toStringTag || "@@toStringTag";

            function wrap(innerFn, outerFn, self, tryLocsList) {
              const protoGenerator = outerFn && outerFn.prototype instanceof Generator ? outerFn : Generator;
              const generator = Object.create(protoGenerator.prototype);
              const context = new Context(tryLocsList || []);
              generator._invoke = makeInvokeMethod(innerFn, self, context);
              return generator;
            }

            exports.wrap = wrap;

            function tryCatch(fn, obj, arg) {
              try {
                return {
                  type: "normal",
                  arg: fn.call(obj, arg)
                };
              } catch (err) {
                return {
                  type: "throw",
                  arg: err
                };
              }
            }

            const GenStateSuspendedStart = "suspendedStart";
            const GenStateSuspendedYield = "suspendedYield";
            const GenStateExecuting = "executing";
            const GenStateCompleted = "completed";
            const ContinueSentinel = {};

            function Generator() {
            }

            function GeneratorFunction() {
            }

            function GeneratorFunctionPrototype() {
            }

            let IteratorPrototype = {};

            IteratorPrototype[iteratorSymbol] = function () {
              return this;
            };

            const getProto = Object.getPrototypeOf;
            const NativeIteratorPrototype = getProto && getProto(getProto(values([])));

            if (NativeIteratorPrototype && NativeIteratorPrototype !== Op && hasOwn.call(NativeIteratorPrototype, iteratorSymbol)) {
              IteratorPrototype = NativeIteratorPrototype;
            }

            const Gp = GeneratorFunctionPrototype.prototype = Generator.prototype = Object.create(IteratorPrototype);
            GeneratorFunction.prototype = Gp.constructor = GeneratorFunctionPrototype;
            GeneratorFunctionPrototype.constructor = GeneratorFunction;
            GeneratorFunctionPrototype[toStringTagSymbol] = GeneratorFunction.displayName = "GeneratorFunction";

            function defineIteratorMethods(prototype) {
              ["next", "throw", "return"].forEach(function (method) {
                prototype[method] = function (arg) {
                  return this._invoke(method, arg);
                };
              });
            }

            exports.isGeneratorFunction = function (genFun) {
              const ctor = typeof genFun === "function" && genFun.constructor;
              return ctor ? ctor === GeneratorFunction || (ctor.displayName || ctor.name) === "GeneratorFunction" : false;
            };

            exports.mark = function (genFun) {
              if (Object.setPrototypeOf) {
                Object.setPrototypeOf(genFun, GeneratorFunctionPrototype);
              } else {
                genFun.__proto__ = GeneratorFunctionPrototype;

                if (!(toStringTagSymbol in genFun)) {
                  genFun[toStringTagSymbol] = "GeneratorFunction";
                }
              }

              genFun.prototype = Object.create(Gp);
              return genFun;
            };

            exports.awrap = function (arg) {
              return {
                __await: arg
              };
            };

            function AsyncIterator(generator) {
              function invoke(method, arg, resolve, reject) {
                const record = tryCatch(generator[method], generator, arg);

                if (record.type === "throw") {
                  reject(record.arg);
                } else {
                  const result = record.arg;
                  const value = result.value;

                  if (value && _typeof(value) === "object" && hasOwn.call(value, "__await")) {
                    return Promise.resolve(value.__await).then(function (value) {
                      invoke("next", value, resolve, reject);
                    }, function (err) {
                      invoke("throw", err, resolve, reject);
                    });
                  }

                  return Promise.resolve(value).then(function (unwrapped) {
                    result.value = unwrapped;
                    resolve(result);
                  }, function (error) {
                    return invoke("throw", error, resolve, reject);
                  });
                }
              }

              let previousPromise;

              function enqueue(method, arg) {
                function callInvokeWithMethodAndArg() {
                  return new Promise(function (resolve, reject) {
                    invoke(method, arg, resolve, reject);
                  });
                }

                return previousPromise = previousPromise ? previousPromise.then(callInvokeWithMethodAndArg, callInvokeWithMethodAndArg) : callInvokeWithMethodAndArg();
              }

              this._invoke = enqueue;
            }

            defineIteratorMethods(AsyncIterator.prototype);

            AsyncIterator.prototype[asyncIteratorSymbol] = function () {
              return this;
            };

            exports.AsyncIterator = AsyncIterator;

            exports.async = function (innerFn, outerFn, self, tryLocsList) {
              const iter = new AsyncIterator(wrap(innerFn, outerFn, self, tryLocsList));
              return exports.isGeneratorFunction(outerFn) ? iter : iter.next().then(function (result) {
                return result.done ? result.value : iter.next();
              });
            };

            function makeInvokeMethod(innerFn, self, context) {
              let state = GenStateSuspendedStart;
              return function invoke(method, arg) {
                if (state === GenStateExecuting) {
                  throw new Error("Generator is already running");
                }

                if (state === GenStateCompleted) {
                  if (method === "throw") {
                    throw arg;
                  }

                  return doneResult();
                }

                context.method = method;
                context.arg = arg;

                while (true) {
                  const delegate = context.delegate;

                  if (delegate) {
                    const delegateResult = maybeInvokeDelegate(delegate, context);

                    if (delegateResult) {
                      if (delegateResult === ContinueSentinel) continue;
                      return delegateResult;
                    }
                  }

                  if (context.method === "next") {
                    context.sent = context._sent = context.arg;
                  } else if (context.method === "throw") {
                    if (state === GenStateSuspendedStart) {
                      state = GenStateCompleted;
                      throw context.arg;
                    }

                    context.dispatchException(context.arg);
                  } else if (context.method === "return") {
                    context.abrupt("return", context.arg);
                  }

                  state = GenStateExecuting;
                  const record = tryCatch(innerFn, self, context);

                  if (record.type === "normal") {
                    state = context.done ? GenStateCompleted : GenStateSuspendedYield;

                    if (record.arg === ContinueSentinel) {
                      continue;
                    }

                    return {
                      value: record.arg,
                      done: context.done
                    };
                  } else if (record.type === "throw") {
                    state = GenStateCompleted;
                    context.method = "throw";
                    context.arg = record.arg;
                  }
                }
              };
            }

            function maybeInvokeDelegate(delegate, context) {
              const method = delegate.iterator[context.method];

              if (method === undefined) {
                context.delegate = null;

                if (context.method === "throw") {
                  if (delegate.iterator["return"]) {
                    context.method = "return";
                    context.arg = undefined;
                    maybeInvokeDelegate(delegate, context);

                    if (context.method === "throw") {
                      return ContinueSentinel;
                    }
                  }

                  context.method = "throw";
                  context.arg = new TypeError("The iterator does not provide a 'throw' method");
                }

                return ContinueSentinel;
              }

              const record = tryCatch(method, delegate.iterator, context.arg);

              if (record.type === "throw") {
                context.method = "throw";
                context.arg = record.arg;
                context.delegate = null;
                return ContinueSentinel;
              }

              const info = record.arg;

              if (!info) {
                context.method = "throw";
                context.arg = new TypeError("iterator result is not an object");
                context.delegate = null;
                return ContinueSentinel;
              }

              if (info.done) {
                context[delegate.resultName] = info.value;
                context.next = delegate.nextLoc;

                if (context.method !== "return") {
                  context.method = "next";
                  context.arg = undefined;
                }
              } else {
                return info;
              }

              context.delegate = null;
              return ContinueSentinel;
            }

            defineIteratorMethods(Gp);
            Gp[toStringTagSymbol] = "Generator";

            Gp[iteratorSymbol] = function () {
              return this;
            };

            Gp.toString = function () {
              return "[object Generator]";
            };

            function pushTryEntry(locs) {
              const entry = {
                tryLoc: locs[0]
              };

              if (1 in locs) {
                entry.catchLoc = locs[1];
              }

              if (2 in locs) {
                entry.finallyLoc = locs[2];
                entry.afterLoc = locs[3];
              }

              this.tryEntries.push(entry);
            }

            function resetTryEntry(entry) {
              const record = entry.completion || {};
              record.type = "normal";
              delete record.arg;
              entry.completion = record;
            }

            function Context(tryLocsList) {
              this.tryEntries = [{
                tryLoc: "root"
              }];
              tryLocsList.forEach(pushTryEntry, this);
              this.reset(true);
            }

            exports.keys = function (object) {
              const keys = [];

              for (var key in object) {
                keys.push(key);
              }

              keys.reverse();
              return function next() {
                while (keys.length) {
                  const key = keys.pop();

                  if (key in object) {
                    next.value = key;
                    next.done = false;
                    return next;
                  }
                }

                next.done = true;
                return next;
              };
            };

            function values(iterable) {
              if (iterable) {
                const iteratorMethod = iterable[iteratorSymbol];

                if (iteratorMethod) {
                  return iteratorMethod.call(iterable);
                }

                if (typeof iterable.next === "function") {
                  return iterable;
                }

                if (!isNaN(iterable.length)) {
                  let i = -1;
                  const next = function next() {
                    while (++i < iterable.length) {
                      if (hasOwn.call(iterable, i)) {
                        next.value = iterable[i];
                        next.done = false;
                        return next;
                      }
                    }

                    next.value = undefined;
                    next.done = true;
                    return next;
                  };

                  return next.next = next;
                }
              }

              return {
                next: doneResult
              };
            }

            exports.values = values;

            function doneResult() {
              return {
                value: undefined,
                done: true
              };
            }

            Context.prototype = {
              constructor: Context,
              reset: function reset(skipTempReset) {
                this.prev = 0;
                this.next = 0;
                this.sent = this._sent = undefined;
                this.done = false;
                this.delegate = null;
                this.method = "next";
                this.arg = undefined;
                this.tryEntries.forEach(resetTryEntry);

                if (!skipTempReset) {
                  for (let name in this) {
                    if (name.charAt(0) === "t" && hasOwn.call(this, name) && !isNaN(+name.slice(1))) {
                      this[name] = undefined;
                    }
                  }
                }
              },
              stop: function stop() {
                this.done = true;
                const rootEntry = this.tryEntries[0];
                const rootRecord = rootEntry.completion;

                if (rootRecord.type === "throw") {
                  throw rootRecord.arg;
                }

                return this.rval;
              },
              dispatchException: function dispatchException(exception) {
                if (this.done) {
                  throw exception;
                }

                const context = this;

                function handle(loc, caught) {
                  record.type = "throw";
                  record.arg = exception;
                  context.next = loc;

                  if (caught) {
                    context.method = "next";
                    context.arg = undefined;
                  }

                  return !!caught;
                }

                for (let i = this.tryEntries.length - 1; i >= 0; --i) {
                  const entry = this.tryEntries[i];
                  var record = entry.completion;

                  if (entry.tryLoc === "root") {
                    return handle("end");
                  }

                  if (entry.tryLoc <= this.prev) {
                    const hasCatch = hasOwn.call(entry, "catchLoc");
                    const hasFinally = hasOwn.call(entry, "finallyLoc");

                    if (hasCatch && hasFinally) {
                      if (this.prev < entry.catchLoc) {
                        return handle(entry.catchLoc, true);
                      } else if (this.prev < entry.finallyLoc) {
                        return handle(entry.finallyLoc);
                      }
                    } else if (hasCatch) {
                      if (this.prev < entry.catchLoc) {
                        return handle(entry.catchLoc, true);
                      }
                    } else if (hasFinally) {
                      if (this.prev < entry.finallyLoc) {
                        return handle(entry.finallyLoc);
                      }
                    } else {
                      throw new Error("try statement without catch or finally");
                    }
                  }
                }
              },
              abrupt: function abrupt(type, arg) {
                for (let i = this.tryEntries.length - 1; i >= 0; --i) {
                  const entry = this.tryEntries[i];

                  if (entry.tryLoc <= this.prev && hasOwn.call(entry, "finallyLoc") && this.prev < entry.finallyLoc) {
                    var finallyEntry = entry;
                    break;
                  }
                }

                if (finallyEntry && (type === "break" || type === "continue") && finallyEntry.tryLoc <= arg && arg <= finallyEntry.finallyLoc) {
                  finallyEntry = null;
                }

                const record = finallyEntry ? finallyEntry.completion : {};
                record.type = type;
                record.arg = arg;

                if (finallyEntry) {
                  this.method = "next";
                  this.next = finallyEntry.finallyLoc;
                  return ContinueSentinel;
                }

                return this.complete(record);
              },
              complete: function complete(record, afterLoc) {
                if (record.type === "throw") {
                  throw record.arg;
                }

                if (record.type === "break" || record.type === "continue") {
                  this.next = record.arg;
                } else if (record.type === "return") {
                  this.rval = this.arg = record.arg;
                  this.method = "return";
                  this.next = "end";
                } else if (record.type === "normal" && afterLoc) {
                  this.next = afterLoc;
                }

                return ContinueSentinel;
              },
              finish: function finish(finallyLoc) {
                for (let i = this.tryEntries.length - 1; i >= 0; --i) {
                  const entry = this.tryEntries[i];

                  if (entry.finallyLoc === finallyLoc) {
                    this.complete(entry.completion, entry.afterLoc);
                    resetTryEntry(entry);
                    return ContinueSentinel;
                  }
                }
              },
              "catch": function _catch(tryLoc) {
                for (let i = this.tryEntries.length - 1; i >= 0; --i) {
                  const entry = this.tryEntries[i];

                  if (entry.tryLoc === tryLoc) {
                    const record = entry.completion;

                    if (record.type === "throw") {
                      var thrown = record.arg;
                      resetTryEntry(entry);
                    }

                    return thrown;
                  }
                }

                throw new Error("illegal catch attempt");
              },
              delegateYield: function delegateYield(iterable, resultName, nextLoc) {
                this.delegate = {
                  iterator: values(iterable),
                  resultName: resultName,
                  nextLoc: nextLoc
                };

                if (this.method === "next") {
                  this.arg = undefined;
                }

                return ContinueSentinel;
              }
            };
            return exports;
          }(_typeof(module) === "object" ? module.exports : {});

          try {
  regeneratorRuntime = runtime;
} catch (accidentalStrictMode) {
  Function("r", "regeneratorRuntime = r")(runtime);
}
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(4)(module)))

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = function (module) {
  if (!module.webpackPolyfill) {
    module.deprecate = function () {};

    module.paths = [];
    if (!module.children) module.children = [];
    Object.defineProperty(module, "loaded", {
      enumerable: true,
      get: function get() {
        return module.l;
      }
    });
    Object.defineProperty(module, "id", {
      enumerable: true,
      get: function get() {
        return module.i;
      }
    });
    module.webpackPolyfill = 1;
  }

  return module;
};

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isValidRotation = isValidRotation;
exports.isValidScrollMode = isValidScrollMode;
exports.isValidSpreadMode = isValidSpreadMode;
exports.isPortraitOrientation = isPortraitOrientation;
exports.getGlobalEventBus = getGlobalEventBus;
exports.getPDFFileNameFromURL = getPDFFileNameFromURL;
exports.noContextMenuHandler = noContextMenuHandler;
exports.parseQueryString = parseQueryString;
exports.backtrackBeforeAllVisibleElements = backtrackBeforeAllVisibleElements;
exports.getVisibleElements = getVisibleElements;
exports.roundToDivide = roundToDivide;
exports.getPageSizeInches = getPageSizeInches;
exports.approximateFraction = approximateFraction;
exports.getOutputScale = getOutputScale;
exports.scrollIntoView = scrollIntoView;
exports.watchScroll = watchScroll;
exports.binarySearchFirstItem = binarySearchFirstItem;
exports.normalizeWheelEventDelta = normalizeWheelEventDelta;
exports.waitOnEventOrTimeout = waitOnEventOrTimeout;
exports.moveToEndOfArray = moveToEndOfArray;
exports.WaitOnType = exports.animationStarted = exports.ProgressBar = exports.EventBus = exports.NullL10n = exports.SpreadMode = exports.ScrollMode = exports.TextLayerMode = exports.RendererType = exports.PresentationModeState = exports.VERTICAL_PADDING = exports.SCROLLBAR_PADDING = exports.MAX_AUTO_SCALE = exports.UNKNOWN_SCALE = exports.MAX_SCALE = exports.MIN_SCALE = exports.DEFAULT_SCALE = exports.DEFAULT_SCALE_VALUE = exports.CSS_UNITS = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _typeof(obj) {
    if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") {
      _typeof = function _typeof(obj) { return typeof obj; };
    } else {
      _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };
    }
    return _typeof(obj);
}

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) { return; }
  const _arr = [];
  let _n = true;
  let _d = false;
  let _e = undefined;
  try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

        const CSS_UNITS = 96.0 / 72.0;
        exports.CSS_UNITS = CSS_UNITS;
        const DEFAULT_SCALE_VALUE = 'auto';
        exports.DEFAULT_SCALE_VALUE = DEFAULT_SCALE_VALUE;
        const DEFAULT_SCALE = 1.0;
        exports.DEFAULT_SCALE = DEFAULT_SCALE;
        const MIN_SCALE = 0.10;
        exports.MIN_SCALE = MIN_SCALE;
        const MAX_SCALE = 10.0;
        exports.MAX_SCALE = MAX_SCALE;
        const UNKNOWN_SCALE = 0;
        exports.UNKNOWN_SCALE = UNKNOWN_SCALE;
        const MAX_AUTO_SCALE = 1.25;
        exports.MAX_AUTO_SCALE = MAX_AUTO_SCALE;
        const SCROLLBAR_PADDING = 40;
        exports.SCROLLBAR_PADDING = SCROLLBAR_PADDING;
        const VERTICAL_PADDING = 5;
        exports.VERTICAL_PADDING = VERTICAL_PADDING;
        const PresentationModeState = {
          UNKNOWN: 0,
          NORMAL: 1,
          CHANGING: 2,
          FULLSCREEN: 3
        };
        exports.PresentationModeState = PresentationModeState;
        const RendererType = {
          CANVAS: 'canvas',
          SVG: 'svg'
        };
        exports.RendererType = RendererType;
        exports.TextLayerMode = {
            DISABLE: 0,
            ENABLE: 1,
            ENABLE_ENHANCE: 2
        };
        const ScrollMode = {
          UNKNOWN: -1,
          VERTICAL: 0,
          HORIZONTAL: 1,
          WRAPPED: 2
        };
        exports.ScrollMode = ScrollMode;
        const SpreadMode = {
          UNKNOWN: -1,
          NONE: 0,
          ODD: 1,
          EVEN: 2
        };
        exports.SpreadMode = SpreadMode;

function formatL10nValue(text, args) {
  if (!args) {
    return text;
  }

  return text.replace(/\{\{\s*(\w+)\s*\}\}/g, function (all, name) {
    return name in args ? args[name] : '{{' + name + '}}';
  });
}

        const NullL10n = {
          getLanguage: function () {
            const _getLanguage = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee() {
                  return _regenerator["default"].wrap(function _callee$(_context) {
                    while (1) {
                      switch (_context.prev = _context.next) {
                        case 0:
                          return _context.abrupt("return", 'en-us');

                        case 1:
                        case "end":
                          return _context.stop();
                      }
                    }
                  }, _callee);
                }));

            function getLanguage() {
              return _getLanguage.apply(this, arguments);
            }

            return getLanguage;
          }(),
          getDirection: function () {
            const _getDirection = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee2() {
                  return _regenerator["default"].wrap(function _callee2$(_context2) {
                    while (1) {
                      switch (_context2.prev = _context2.next) {
                        case 0:
                          return _context2.abrupt("return", 'ltr');

                        case 1:
                        case "end":
                          return _context2.stop();
                      }
                    }
                  }, _callee2);
                }));

            function getDirection() {
              return _getDirection.apply(this, arguments);
            }

            return getDirection;
          }(),
          get: function () {
            const _get = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee3(property, args, fallback) {
                  return _regenerator["default"].wrap(function _callee3$(_context3) {
                    while (1) {
                      switch (_context3.prev = _context3.next) {
                        case 0:
                          return _context3.abrupt("return", formatL10nValue(fallback, args));

                        case 1:
                        case "end":
                          return _context3.stop();
                      }
                    }
                  }, _callee3);
                }));

            function get(_x, _x2, _x3) {
              return _get.apply(this, arguments);
            }

            return get;
          }(),
          translate: function () {
            const _translate = _asyncToGenerator(
                /*#__PURE__*/
                _regenerator["default"].mark(function _callee4(element) {
                  return _regenerator["default"].wrap(function _callee4$(_context4) {
                    while (1) {
                      switch (_context4.prev = _context4.next) {
                        case 0:
                        case "end":
                          return _context4.stop();
                      }
                    }
                  }, _callee4);
                }));

            function translate(_x4) {
              return _translate.apply(this, arguments);
            }

            return translate;
          }()
        };
        exports.NullL10n = NullL10n;

function getOutputScale(ctx) {
  const devicePixelRatio = window.devicePixelRatio || 1;
  const backingStoreRatio = ctx.webkitBackingStorePixelRatio || ctx.mozBackingStorePixelRatio || ctx.msBackingStorePixelRatio || ctx.oBackingStorePixelRatio || ctx.backingStorePixelRatio || 1;
  const pixelRatio = devicePixelRatio / backingStoreRatio;
  return {
    sx: pixelRatio,
    sy: pixelRatio,
    scaled: pixelRatio !== 1
  };
}

function scrollIntoView(element, spot) {
  const skipOverflowHiddenElements = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
  let parent = element.offsetParent;

  if (!parent) {
    console.error('offsetParent is not set -- cannot scroll');
    return;
  }

  let offsetY = element.offsetTop + element.clientTop;
  let offsetX = element.offsetLeft + element.clientLeft;

  while (parent.clientHeight === parent.scrollHeight && parent.clientWidth === parent.scrollWidth || skipOverflowHiddenElements && getComputedStyle(parent).overflow === 'hidden') {
    if (parent.dataset._scaleY) {
      offsetY /= parent.dataset._scaleY;
      offsetX /= parent.dataset._scaleX;
    }

    offsetY += parent.offsetTop;
    offsetX += parent.offsetLeft;
    parent = parent.offsetParent;

    if (!parent) {
      return;
    }
  }

  if (spot) {
    if (spot.top !== undefined) {
      offsetY += spot.top;
    }

    if (spot.left !== undefined) {
      offsetX += spot.left;
      parent.scrollLeft = offsetX;
    }
  }

  parent.scrollTop = offsetY;
}

function watchScroll(viewAreaElement, callback) {
  const debounceScroll = function debounceScroll(evt) {
    if (rAF) {
      return;
    }

    rAF = window.requestAnimationFrame(function viewAreaElementScrolled() {
      rAF = null;
      const currentX = viewAreaElement.scrollLeft;
      const lastX = state.lastX;

      if (currentX !== lastX) {
        state.right = currentX > lastX;
      }

      state.lastX = currentX;
      const currentY = viewAreaElement.scrollTop;
      const lastY = state.lastY;

      if (currentY !== lastY) {
        state.down = currentY > lastY;
      }

      state.lastY = currentY;
      callback(state);
    });
  };

  var state = {
    right: true,
    down: true,
    lastX: viewAreaElement.scrollLeft,
    lastY: viewAreaElement.scrollTop,
    _eventHandler: debounceScroll
  };
  var rAF = null;
  viewAreaElement.addEventListener('scroll', debounceScroll, true);
  return state;
}

function parseQueryString(query) {
  const parts = query.split('&');
  const params = Object.create(null);

  let i = 0;
  const ii = parts.length;
  for (; i < ii; ++i) {
    const param = parts[i].split('=');
    const key = param[0].toLowerCase();
    const value = param.length > 1 ? param[1] : null;
    params[decodeURIComponent(key)] = decodeURIComponent(value);
  }

  return params;
}

function binarySearchFirstItem(items, condition) {
  let minIndex = 0;
  let maxIndex = items.length - 1;

  if (items.length === 0 || !condition(items[maxIndex])) {
    return items.length;
  }

  if (condition(items[minIndex])) {
    return minIndex;
  }

  while (minIndex < maxIndex) {
    const currentIndex = minIndex + maxIndex >> 1;
    const currentItem = items[currentIndex];

    if (condition(currentItem)) {
      maxIndex = currentIndex;
    } else {
      minIndex = currentIndex + 1;
    }
  }

  return minIndex;
}

function approximateFraction(x) {
  if (Math.floor(x) === x) {
    return [x, 1];
  }

  const xinv = 1 / x;
  const limit = 8;

  if (xinv > limit) {
    return [1, limit];
  } else if (Math.floor(xinv) === xinv) {
    return [1, xinv];
  }

  const x_ = x > 1 ? xinv : x;
  let a = 0,
      b = 1,
      c = 1,
      d = 1;

  while (true) {
    const p = a + c,
        q = b + d;

    if (q > limit) {
      break;
    }

    if (x_ <= p / q) {
      c = p;
      d = q;
    } else {
      a = p;
      b = q;
    }
  }

  let result;

  if (x_ - a / b < c / d - x_) {
    result = x_ === x ? [a, b] : [b, a];
  } else {
    result = x_ === x ? [c, d] : [d, c];
  }

  return result;
}

function roundToDivide(x, div) {
  const r = x % div;
  return r === 0 ? x : Math.round(x - r + div);
}

function getPageSizeInches(_ref) {
  const view = _ref.view,
      userUnit = _ref.userUnit,
      rotate = _ref.rotate;

  const _view = _slicedToArray(view, 4),
      x1 = _view[0],
      y1 = _view[1],
      x2 = _view[2],
      y2 = _view[3];

  const changeOrientation = rotate % 180 !== 0;
  const width = (x2 - x1) / 72 * userUnit;
  const height = (y2 - y1) / 72 * userUnit;
  return {
    width: changeOrientation ? height : width,
    height: changeOrientation ? width : height
  };
}

function backtrackBeforeAllVisibleElements(index, views, top) {
  if (index < 2) {
    return index;
  }

  let elt = views[index].div;
  let pageTop = elt.offsetTop + elt.clientTop;

  if (pageTop >= top) {
    elt = views[index - 1].div;
    pageTop = elt.offsetTop + elt.clientTop;
  }

  for (let i = index - 2; i >= 0; --i) {
    elt = views[i].div;

    if (elt.offsetTop + elt.clientTop + elt.clientHeight <= pageTop) {
      break;
    }

    index = i;
  }

  return index;
}

function getVisibleElements(scrollEl, views) {
  const sortByVisibility = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
  const horizontal = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
  const top = scrollEl.scrollTop,
      bottom = top + scrollEl.clientHeight;
  const left = scrollEl.scrollLeft,
      right = left + scrollEl.clientWidth;

  function isElementBottomAfterViewTop(view) {
    const element = view.div;
    const elementBottom = element.offsetTop + element.clientTop + element.clientHeight;
    return elementBottom > top;
  }

  function isElementRightAfterViewLeft(view) {
    const element = view.div;
    const elementRight = element.offsetLeft + element.clientLeft + element.clientWidth;
    return elementRight > left;
  }

  const visible = [],
      numViews = views.length;
  let firstVisibleElementInd = numViews === 0 ? 0 : binarySearchFirstItem(views, horizontal ? isElementRightAfterViewLeft : isElementBottomAfterViewTop);

  if (firstVisibleElementInd > 0 && firstVisibleElementInd < numViews && !horizontal) {
    firstVisibleElementInd = backtrackBeforeAllVisibleElements(firstVisibleElementInd, views, top);
  }

  let lastEdge = horizontal ? right : -1;

  for (let i = firstVisibleElementInd; i < numViews; i++) {
    var view = views[i],
        element = view.div;
    const currentWidth = element.offsetLeft + element.clientLeft;
    const currentHeight = element.offsetTop + element.clientTop;
    const viewWidth = element.clientWidth,
        viewHeight = element.clientHeight;
    const viewRight = currentWidth + viewWidth;
    const viewBottom = currentHeight + viewHeight;

    if (lastEdge === -1) {
      if (viewBottom >= bottom) {
        lastEdge = viewBottom;
      }
    } else if ((horizontal ? currentWidth : currentHeight) > lastEdge) {
      break;
    }

    if (viewBottom <= top || currentHeight >= bottom || viewRight <= left || currentWidth >= right) {
      continue;
    }

    const hiddenHeight = Math.max(0, top - currentHeight) + Math.max(0, viewBottom - bottom);
    const hiddenWidth = Math.max(0, left - currentWidth) + Math.max(0, viewRight - right);
    const percent = (viewHeight - hiddenHeight) * (viewWidth - hiddenWidth) * 100 / viewHeight / viewWidth | 0;
    visible.push({
      id: view.id,
      x: currentWidth,
      y: currentHeight,
      view: view,
      percent: percent
    });
  }

  const first = visible[0],
      last = visible[visible.length - 1];

  if (sortByVisibility) {
    visible.sort(function (a, b) {
      const pc = a.percent - b.percent;

      if (Math.abs(pc) > 0.001) {
        return -pc;
      }

      return a.id - b.id;
    });
  }

  return {
    first: first,
    last: last,
    views: visible
  };
}

function noContextMenuHandler(evt) {
  evt.preventDefault();
}

function isDataSchema(url) {
  let i = 0;
  const ii = url.length;

  while (i < ii && url[i].trim() === '') {
    i++;
  }

  return url.substring(i, i + 5).toLowerCase() === 'data:';
}

function getPDFFileNameFromURL(url) {
  const defaultFilename = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'document.pdf';

  if (typeof url !== 'string') {
    return defaultFilename;
  }

  if (isDataSchema(url)) {
    console.warn('getPDFFileNameFromURL: ' + 'ignoring "data:" URL for performance reasons.');
    return defaultFilename;
  }

  const reURI = /^(?:(?:[^:]+:)?\/\/[^\/]+)?([^?#]*)(\?[^#]*)?(#.*)?$/;
  const reFilename = /[^\/?#=]+\.pdf\b(?!.*\.pdf\b)/i;
  const splitURI = reURI.exec(url);
  let suggestedFilename = reFilename.exec(splitURI[1]) || reFilename.exec(splitURI[2]) || reFilename.exec(splitURI[3]);

  if (suggestedFilename) {
    suggestedFilename = suggestedFilename[0];

    if (suggestedFilename.includes('%')) {
      try {
        suggestedFilename = reFilename.exec(decodeURIComponent(suggestedFilename))[0];
      } catch (ex) {}
    }
  }

  return suggestedFilename || defaultFilename;
}

function normalizeWheelEventDelta(evt) {
  let delta = Math.sqrt(evt.deltaX * evt.deltaX + evt.deltaY * evt.deltaY);
  const angle = Math.atan2(evt.deltaY, evt.deltaX);

  if (-0.25 * Math.PI < angle && angle < 0.75 * Math.PI) {
    delta = -delta;
  }

  const MOUSE_DOM_DELTA_PIXEL_MODE = 0;
  const MOUSE_DOM_DELTA_LINE_MODE = 1;
  const MOUSE_PIXELS_PER_LINE = 30;
  const MOUSE_LINES_PER_PAGE = 30;

  if (evt.deltaMode === MOUSE_DOM_DELTA_PIXEL_MODE) {
    delta /= MOUSE_PIXELS_PER_LINE * MOUSE_LINES_PER_PAGE;
  } else if (evt.deltaMode === MOUSE_DOM_DELTA_LINE_MODE) {
    delta /= MOUSE_LINES_PER_PAGE;
  }

  return delta;
}

function isValidRotation(angle) {
  return Number.isInteger(angle) && angle % 90 === 0;
}

function isValidScrollMode(mode) {
  return Number.isInteger(mode) && Object.values(ScrollMode).includes(mode) && mode !== ScrollMode.UNKNOWN;
}

function isValidSpreadMode(mode) {
  return Number.isInteger(mode) && Object.values(SpreadMode).includes(mode) && mode !== SpreadMode.UNKNOWN;
}

function isPortraitOrientation(size) {
  return size.width <= size.height;
}

        const WaitOnType = {
          EVENT: 'event',
          TIMEOUT: 'timeout'
        };
        exports.WaitOnType = WaitOnType;

function waitOnEventOrTimeout(_ref2) {
  const target = _ref2.target,
      name = _ref2.name,
      _ref2$delay = _ref2.delay,
      delay = _ref2$delay === void 0 ? 0 : _ref2$delay;
  return new Promise(function (resolve, reject) {
    if (_typeof(target) !== 'object' || !(name && typeof name === 'string') || !(Number.isInteger(delay) && delay >= 0)) {
      throw new Error('waitOnEventOrTimeout - invalid parameters.');
    }

    function handler(type) {
      if (target instanceof EventBus) {
        target.off(name, eventHandler);
      } else {
        target.removeEventListener(name, eventHandler);
      }

      if (timeout) {
        clearTimeout(timeout);
      }

      resolve(type);
    }

    var eventHandler = handler.bind(null, WaitOnType.EVENT);

    if (target instanceof EventBus) {
      target.on(name, eventHandler);
    } else {
      target.addEventListener(name, eventHandler);
    }

    const timeoutHandler = handler.bind(null, WaitOnType.TIMEOUT);
    var timeout = setTimeout(timeoutHandler, delay);
  }).catch(error => {
    console.log("hello i cannot event stringify");
    console.warn("i am a caught error:" + error);
    throw error;
  });
}

        const animationStarted = new Promise(function (resolve) {
          window.requestAnimationFrame(resolve);
        });
        exports.animationStarted = animationStarted;

var EventBus =
/*#__PURE__*/
function () {
  function EventBus() {
    const _ref3 = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
        _ref3$dispatchToDOM = _ref3.dispatchToDOM,
        dispatchToDOM = _ref3$dispatchToDOM === void 0 ? false : _ref3$dispatchToDOM;

    _classCallCheck(this, EventBus);

    this._listeners = Object.create(null);
    this._dispatchToDOM = dispatchToDOM === true;
  }

  _createClass(EventBus, [{
    key: "on",
    value: function on(eventName, listener) {
      let eventListeners = this._listeners[eventName];

      if (!eventListeners) {
        eventListeners = [];
        this._listeners[eventName] = eventListeners;
      }

      eventListeners.push(listener);
    }
  }, {
    key: "off",
    value: function off(eventName, listener) {
      const eventListeners = this._listeners[eventName];
      let i;

      if (!eventListeners || (i = eventListeners.indexOf(listener)) < 0) {
        return;
      }

      eventListeners.splice(i, 1);
    }
  }, {
    key: "dispatch",
    value: function dispatch(eventName) {
      const eventListeners = this._listeners[eventName];

      if (!eventListeners || eventListeners.length === 0) {
        if (this._dispatchToDOM) {
          const _args5 = Array.prototype.slice.call(arguments, 1);

          this._dispatchDOMEvent(eventName, _args5);
        }

        return;
      }

      const args = Array.prototype.slice.call(arguments, 1);
      eventListeners.slice(0).forEach(function (listener) {
        listener.apply(null, args);
      });

      if (this._dispatchToDOM) {
        this._dispatchDOMEvent(eventName, args);
      }
    }
  }, {
    key: "_dispatchDOMEvent",
    value: function _dispatchDOMEvent(eventName) {
      const args = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
      const details = Object.create(null);

      if (args && args.length > 0) {
        const obj = args[0];

        for (let key in obj) {
          const value = obj[key];

          if (key === 'source') {
            if (value === window || value === document) {
              return;
            }

            continue;
          }

          details[key] = value;
        }
      }

      const event = document.createEvent('CustomEvent');
      event.initCustomEvent(eventName, true, true, details);
      document.dispatchEvent(event);
    }
  }]);

  return EventBus;
}();

exports.EventBus = EventBus;
        let globalEventBus = null;

        function getGlobalEventBus() {
  const dispatchToDOM = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

  if (!globalEventBus) {
    globalEventBus = new EventBus({
      dispatchToDOM: dispatchToDOM
    });
  }

  return globalEventBus;
}

function clamp(v, min, max) {
  return Math.min(Math.max(v, min), max);
}

        const ProgressBar =
            /*#__PURE__*/
            function () {
              function ProgressBar(id) {
                const _ref4 = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
                    height = _ref4.height,
                    width = _ref4.width,
                    units = _ref4.units;

                _classCallCheck(this, ProgressBar);

                this.visible = true;
                this.div = document.querySelector(id + ' .progress');
                this.bar = this.div.parentNode;
                this.height = height || 100;
                this.width = width || 100;
                this.units = units || '%';
                this.div.style.height = this.height + this.units;
                this.percent = 0;
              }

              _createClass(ProgressBar, [{
                key: "_updateBar",
                value: function _updateBar() {
                  if (this._indeterminate) {
                    this.div.classList.add('indeterminate');
                    this.div.style.width = this.width + this.units;
                    return;
                  }

                  this.div.classList.remove('indeterminate');
                  const progressSize = this.width * this._percent / 100;
                  this.div.style.width = progressSize + this.units;
                }
              }, {
                key: "setWidth",
                value: function setWidth(viewer) {
                  if (!viewer) {
                    return;
                  }

                  const container = viewer.parentNode;
                  const scrollbarWidth = container.offsetWidth - viewer.offsetWidth;

                  if (scrollbarWidth > 0) {
                    this.bar.setAttribute('style', 'width: calc(100% - ' + scrollbarWidth + 'px);');
                  }
                }
              }, {
                key: "hide",
                value: function hide() {
                  if (!this.visible) {
                    return;
                  }

                  this.visible = false;
                  this.bar.classList.add('hidden');
                  document.body.classList.remove('loadingInProgress');
                }
              }, {
                key: "show",
                value: function show() {
                  if (this.visible) {
                    return;
                  }

                  this.visible = true;
                  document.body.classList.add('loadingInProgress');
                  this.bar.classList.remove('hidden');
                }
              }, {
                key: "percent",
                get: function get() {
                  return this._percent;
                },
                set: function set(val) {
                  this._indeterminate = isNaN(val);
                  this._percent = clamp(val, 0, 100);

                  this._updateBar();
                }
              }]);

              return ProgressBar;
            }();

        exports.ProgressBar = ProgressBar;

function moveToEndOfArray(arr, condition) {
  const moved = [],
      len = arr.length;
  let write = 0;

  for (let read = 0; read < len; ++read) {
    if (condition(arr[read])) {
      moved.push(arr[read]);
    } else {
      arr[write] = arr[read];
      ++write;
    }
  }

  for (let _read = 0; write < len; ++_read, ++write) {
    arr[write] = moved[_read];
  }
}

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.OptionKind = exports.AppOptions = void 0;

        const _pdfjsLib = __webpack_require__(7);

        const _viewer_compatibility = __webpack_require__(8);

        function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const OptionKind = {
          VIEWER: 0x02,
          API: 0x04,
          WORKER: 0x08,
          PREFERENCE: 0x80
        };
        exports.OptionKind = OptionKind;
        const defaultOptions = {
          cursorToolOnLoad: {
            value: 0,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          defaultUrl: {
            value: 'compressed.tracemonkey-pldi-09.pdf',
            kind: OptionKind.VIEWER
          },
          defaultZoomValue: {
            value: '',
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          disableHistory: {
            value: false,
            kind: OptionKind.VIEWER
          },
          disablePageLabels: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          enablePrintAutoRotate: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          enableWebGL: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          eventBusDispatchToDOM: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          externalLinkRel: {
            value: 'noopener noreferrer nofollow',
            kind: OptionKind.VIEWER
          },
          externalLinkTarget: {
            value: 0,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          historyUpdateUrl: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          imageResourcesPath: {
            value: './images/',
            kind: OptionKind.VIEWER
          },
          maxCanvasPixels: {
            value: 16777216,
            compatibility: _viewer_compatibility.viewerCompatibilityParams.maxCanvasPixels,
            kind: OptionKind.VIEWER
          },
          pdfBugEnabled: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          renderer: {
            value: 'canvas',
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          renderInteractiveForms: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          sidebarViewOnLoad: {
            value: -1,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          scrollModeOnLoad: {
            value: -1,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          spreadModeOnLoad: {
            value: -1,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          textLayerMode: {
            value: 1,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          useOnlyCssZoom: {
            value: false,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          viewOnLoad: {
            value: 0,
            kind: OptionKind.VIEWER + OptionKind.PREFERENCE
          },
          cMapPacked: {
            value: true,
            kind: OptionKind.API
          },
          cMapUrl: {
            value: '../web/cmaps/',
            kind: OptionKind.API
          },
          disableAutoFetch: {
            value: false,
            kind: OptionKind.API + OptionKind.PREFERENCE
          },
          disableCreateObjectURL: {
            value: false,
            compatibility: _pdfjsLib.apiCompatibilityParams.disableCreateObjectURL,
            kind: OptionKind.API
          },
          disableFontFace: {
            value: false,
            kind: OptionKind.API + OptionKind.PREFERENCE
          },
          disableRange: {
            value: false,
            kind: OptionKind.API + OptionKind.PREFERENCE
          },
          disableStream: {
            value: false,
            kind: OptionKind.API + OptionKind.PREFERENCE
          },
          docBaseUrl: {
            value: '',
            kind: OptionKind.API
          },
          isEvalSupported: {
            value: true,
            kind: OptionKind.API
          },
          maxImageSize: {
            value: -1,
            kind: OptionKind.API
          },
          pdfBug: {
            value: false,
            kind: OptionKind.API
          },
          verbosity: {
            value: 1,
            kind: OptionKind.API
          },
          workerPort: {
            value: null,
            kind: OptionKind.WORKER
          },
          workerSrc: {
            value: '../build/pdf.worker.js',
            kind: OptionKind.WORKER
          }
        };
        {
  defaultOptions.disablePreferences = {
    value: false,
    kind: OptionKind.VIEWER
  };
  defaultOptions.locale = {
    value: typeof navigator !== 'undefined' ? navigator.language : 'en-US',
    kind: OptionKind.VIEWER
  };
  defaultOptions.printResolution = {
    value: 150,
    kind: OptionKind.VIEWER
  };
}
        const userOptions = Object.create(null);

        const AppOptions =
            /*#__PURE__*/
            function () {
              function AppOptions() {
                _classCallCheck(this, AppOptions);

                throw new Error('Cannot initialize AppOptions.');
              }

              _createClass(AppOptions, null, [{
                key: "get",
                value: function get(name) {
                  const userOption = userOptions[name];

                  if (userOption !== undefined) {
                    return userOption;
                  }

                  const defaultOption = defaultOptions[name];

                  if (defaultOption !== undefined) {
                    return defaultOption.compatibility || defaultOption.value;
                  }

                  return undefined;
                }
              }, {
                key: "getAll",
                value: function getAll() {
                  const kind = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : null;
                  const options = Object.create(null);

                  for (let name in defaultOptions) {
                    const defaultOption = defaultOptions[name];

                    if (kind) {
                      if ((kind & defaultOption.kind) === 0) {
                        continue;
                      }

                      if (kind === OptionKind.PREFERENCE) {
                        const value = defaultOption.value,
                            valueType = _typeof(value);

                        if (valueType === 'boolean' || valueType === 'string' || valueType === 'number' && Number.isInteger(value)) {
                          options[name] = value;
                          continue;
                        }

                        throw new Error("Invalid type for preference: ".concat(name));
                      }
                    }

                    const userOption = userOptions[name];
                    options[name] = userOption !== undefined ? userOption : defaultOption.compatibility || defaultOption.value;
                  }

                  return options;
                }
              }, {
                key: "set",
                value: function set(name, value) {
                  userOptions[name] = value;
                }
              }, {
                key: "remove",
                value: function remove(name) {
                  delete userOptions[name];
                }
              }]);

              return AppOptions;
            }();

        exports.AppOptions = AppOptions;

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


        let pdfjsLib;

        if (typeof window !== 'undefined' && window['pdfjs-dist/build/pdf']) {
  pdfjsLib = window['pdfjs-dist/build/pdf'];
} else {
  pdfjsLib = require('../build/pdf.js');
}

module.exports = pdfjsLib;

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


        const compatibilityParams = Object.create(null);
        {
  const userAgent = typeof navigator !== 'undefined' && navigator.userAgent || '';
  const isAndroid = /Android/.test(userAgent);
  const isIOS = /\b(iPad|iPhone|iPod)(?=;)/.test(userAgent);

  (function checkCanvasSizeLimitation() {
    if (isIOS || isAndroid) {
      compatibilityParams.maxCanvasPixels = 5242880;
    }
  })();
}
exports.viewerCompatibilityParams = Object.freeze(compatibilityParams);

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFCursorTools = exports.CursorTool = void 0;

        const _grab_to_pan = __webpack_require__(10);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const CursorTool = {
          SELECT: 0,
          HAND: 1,
          ZOOM: 2
        };
        exports.CursorTool = CursorTool;

        const PDFCursorTools =
            /*#__PURE__*/
            function () {
              function PDFCursorTools(_ref) {
                const _this = this;

                const container = _ref.container,
                    eventBus = _ref.eventBus,
                    _ref$cursorToolOnLoad = _ref.cursorToolOnLoad,
                    cursorToolOnLoad = _ref$cursorToolOnLoad === void 0 ? CursorTool.SELECT : _ref$cursorToolOnLoad;

                _classCallCheck(this, PDFCursorTools);

                this.container = container;
                this.eventBus = eventBus;
                this.active = CursorTool.SELECT;
                this.activeBeforePresentationMode = null;
                this.handTool = new _grab_to_pan.GrabToPan({
                  element: this.container
                });

                this._addEventListeners();

                Promise.resolve().then(function () {
                  _this.switchTool(cursorToolOnLoad);
                });
              }

              _createClass(PDFCursorTools, [{
                key: "switchTool",
                value: function switchTool(tool) {
                  const _this2 = this;

                  if (this.activeBeforePresentationMode !== null) {
                    return;
                  }

                  if (tool === this.active) {
                    return;
                  }

                  const disableActiveTool = function disableActiveTool() {
                    switch (_this2.active) {
                      case CursorTool.SELECT:
                        break;

                      case CursorTool.HAND:
                        _this2.handTool.deactivate();

                        break;

                      case CursorTool.ZOOM:
                    }
                  };

                  switch (tool) {
                    case CursorTool.SELECT:
                      disableActiveTool();
                      break;

                    case CursorTool.HAND:
                      disableActiveTool();
                      this.handTool.activate();
                      break;

                    case CursorTool.ZOOM:
                    default:
                      console.error("switchTool: \"".concat(tool, "\" is an unsupported value."));
                      return;
                  }

                  this.active = tool;

                  this._dispatchEvent();
                }
              }, {
                key: "_dispatchEvent",
                value: function _dispatchEvent() {
                  this.eventBus.dispatch('cursortoolchanged', {
                    source: this,
                    tool: this.active
                  });
                }
              }, {
                key: "_addEventListeners",
                value: function _addEventListeners() {
                  const _this3 = this;

                  this.eventBus.on('switchcursortool', function (evt) {
                    _this3.switchTool(evt.tool);
                  });
                  this.eventBus.on('presentationmodechanged', function (evt) {
                    if (evt.switchInProgress) {
                      return;
                    }

                    let previouslyActive;

                    if (evt.active) {
                      previouslyActive = _this3.active;

                      _this3.switchTool(CursorTool.SELECT);

                      _this3.activeBeforePresentationMode = previouslyActive;
                    } else {
                      previouslyActive = _this3.activeBeforePresentationMode;
                      _this3.activeBeforePresentationMode = null;

                      _this3.switchTool(previouslyActive);
                    }
                  });
                }
              }, {
                key: "activeTool",
                get: function get() {
                  return this.active;
                }
              }]);

              return PDFCursorTools;
            }();

        exports.PDFCursorTools = PDFCursorTools;

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.GrabToPan = GrabToPan;

function GrabToPan(options) {
  this.element = options.element;
  this.document = options.element.ownerDocument;

  if (typeof options.ignoreTarget === 'function') {
    this.ignoreTarget = options.ignoreTarget;
  }

  this.onActiveChanged = options.onActiveChanged;
  this.activate = this.activate.bind(this);
  this.deactivate = this.deactivate.bind(this);
  this.toggle = this.toggle.bind(this);
  this._onmousedown = this._onmousedown.bind(this);
  this._onmousemove = this._onmousemove.bind(this);
  this._endPan = this._endPan.bind(this);
  const overlay = this.overlay = document.createElement('div');
  overlay.className = 'grab-to-pan-grabbing';
}

GrabToPan.prototype = {
  CSS_CLASS_GRAB: 'grab-to-pan-grab',
  activate: function GrabToPan_activate() {
    if (!this.active) {
      this.active = true;
      this.element.addEventListener('mousedown', this._onmousedown, true);
      this.element.classList.add(this.CSS_CLASS_GRAB);

      if (this.onActiveChanged) {
        this.onActiveChanged(true);
      }
    }
  },
  deactivate: function GrabToPan_deactivate() {
    if (this.active) {
      this.active = false;
      this.element.removeEventListener('mousedown', this._onmousedown, true);

      this._endPan();

      this.element.classList.remove(this.CSS_CLASS_GRAB);

      if (this.onActiveChanged) {
        this.onActiveChanged(false);
      }
    }
  },
  toggle: function GrabToPan_toggle() {
    if (this.active) {
      this.deactivate();
    } else {
      this.activate();
    }
  },
  ignoreTarget: function GrabToPan_ignoreTarget(node) {
    return node[matchesSelector]('a[href], a[href] *, input, textarea, button, button *, select, option');
  },
  _onmousedown: function GrabToPan__onmousedown(event) {
    if (event.button !== 0 || this.ignoreTarget(event.target)) {
      return;
    }

    if (event.originalTarget) {
      try {
        event.originalTarget.tagName;
      } catch (e) {
        return;
      }
    }

    this.scrollLeftStart = this.element.scrollLeft;
    this.scrollTopStart = this.element.scrollTop;
    this.clientXStart = event.clientX;
    this.clientYStart = event.clientY;
    this.document.addEventListener('mousemove', this._onmousemove, true);
    this.document.addEventListener('mouseup', this._endPan, true);
    this.element.addEventListener('scroll', this._endPan, true);
    event.preventDefault();
    event.stopPropagation();
    const focusedElement = document.activeElement;

    if (focusedElement && !focusedElement.contains(event.target)) {
      focusedElement.blur();
    }
  },
  _onmousemove: function GrabToPan__onmousemove(event) {
    this.element.removeEventListener('scroll', this._endPan, true);

    if (isLeftMouseReleased(event)) {
      this._endPan();

      return;
    }

    const xDiff = event.clientX - this.clientXStart;
    const yDiff = event.clientY - this.clientYStart;
    const scrollTop = this.scrollTopStart - yDiff;
    const scrollLeft = this.scrollLeftStart - xDiff;

    if (this.element.scrollTo) {
      this.element.scrollTo({
        top: scrollTop,
        left: scrollLeft,
        behavior: 'instant'
      });
    } else {
      this.element.scrollTop = scrollTop;
      this.element.scrollLeft = scrollLeft;
    }

    if (!this.overlay.parentNode) {
      document.body.appendChild(this.overlay);
    }
  },
  _endPan: function GrabToPan__endPan() {
    this.element.removeEventListener('scroll', this._endPan, true);
    this.document.removeEventListener('mousemove', this._onmousemove, true);
    this.document.removeEventListener('mouseup', this._endPan, true);
    this.overlay.remove();
  }
};
var matchesSelector;
['webkitM', 'mozM', 'msM', 'oM', 'm'].some(function (prefix) {
  let name = prefix + 'atches';

  if (name in document.documentElement) {
    matchesSelector = name;
  }

  name += 'Selector';

  if (name in document.documentElement) {
    matchesSelector = name;
  }

  return matchesSelector;
});
        const isNotIEorIsIE10plus = !document.documentMode || document.documentMode > 9;
        const chrome = window.chrome;
        const isChrome15OrOpera15plus = chrome && (chrome.webstore || chrome.app);
        const isSafari6plus = /Apple/.test(navigator.vendor) && /Version\/([6-9]\d*|[1-5]\d+)/.test(navigator.userAgent);

        function isLeftMouseReleased(event) {
  if ('buttons' in event && isNotIEorIsIE10plus) {
    return !(event.buttons & 1);
  }

  if (isChrome15OrOpera15plus || isSafari6plus) {
    return event.which === 0;
  }

  return false;
}

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFRenderingQueue = exports.RenderingStates = void 0;

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const CLEANUP_TIMEOUT = 30000;
        const RenderingStates = {
          INITIAL: 0,
          RUNNING: 1,
          PAUSED: 2,
          FINISHED: 3
        };
        exports.RenderingStates = RenderingStates;

        const PDFRenderingQueue =
            /*#__PURE__*/
            function () {
              function PDFRenderingQueue() {
                _classCallCheck(this, PDFRenderingQueue);

                this.pdfViewer = null;
                this.pdfThumbnailViewer = null;
                this.onIdle = null;
                this.highestPriorityPage = null;
                this.idleTimeout = null;
                this.printing = false;
                this.isThumbnailViewEnabled = false;
              }

              _createClass(PDFRenderingQueue, [{
                key: "setViewer",
                value: function setViewer(pdfViewer) {
                  this.pdfViewer = pdfViewer;
                }
              }, {
                key: "setThumbnailViewer",
                value: function setThumbnailViewer(pdfThumbnailViewer) {
                  this.pdfThumbnailViewer = pdfThumbnailViewer;
                }
              }, {
                key: "isHighestPriority",
                value: function isHighestPriority(view) {
                  return this.highestPriorityPage === view.renderingId;
                }
              }, {
                key: "renderHighestPriority",
                value: function renderHighestPriority(currentlyVisiblePages) {
                  if (this.idleTimeout) {
                    clearTimeout(this.idleTimeout);
                    this.idleTimeout = null;
                  }

                  if (this.pdfViewer.forceRendering(currentlyVisiblePages)) {
                    return;
                  }

                  if (this.pdfThumbnailViewer && this.isThumbnailViewEnabled) {
                    if (this.pdfThumbnailViewer.forceRendering()) {
                      return;
                    }
                  }

                  if (this.printing) {
                    return;
                  }

                  if (this.onIdle) {
                    this.idleTimeout = setTimeout(this.onIdle.bind(this), CLEANUP_TIMEOUT);
                  }
                }
              }, {
                key: "getHighestPriority",
                value: function getHighestPriority(visible, views, scrolledDown) {
                  const visibleViews = visible.views;
                  const numVisible = visibleViews.length;

                  if (numVisible === 0) {
                    return null;
                  }

                  for (let i = 0; i < numVisible; ++i) {
                    const view = visibleViews[i].view;

                    if (!this.isViewFinished(view)) {
                      return view;
                    }
                  }

                  if (scrolledDown) {
                    const nextPageIndex = visible.last.id;

                    if (views[nextPageIndex] && !this.isViewFinished(views[nextPageIndex])) {
                      return views[nextPageIndex];
                    }
                  } else {
                    const previousPageIndex = visible.first.id - 2;

                    if (views[previousPageIndex] && !this.isViewFinished(views[previousPageIndex])) {
                      return views[previousPageIndex];
                    }
                  }

                  return null;
                }
              }, {
                key: "isViewFinished",
                value: function isViewFinished(view) {
                  return view.renderingState === RenderingStates.FINISHED;
                }
              }, {
                key: "renderView",
                value: function renderView(view) {
                  const _this = this;

                  switch (view.renderingState) {
                    case RenderingStates.FINISHED:
                      return false;

                    case RenderingStates.PAUSED:
                      this.highestPriorityPage = view.renderingId;
                      view.resume();
                      break;

                    case RenderingStates.RUNNING:
                      this.highestPriorityPage = view.renderingId;
                      break;

                    case RenderingStates.INITIAL:
                      this.highestPriorityPage = view.renderingId;
                      view.draw()["finally"](function () {
                        _this.renderHighestPriority();
                      });
                      break;
                  }

                  return true;
                }
              }]);

              return PDFRenderingQueue;
            }();

        exports.PDFRenderingQueue = PDFRenderingQueue;

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFSidebar = exports.SidebarView = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdf_rendering_queue = __webpack_require__(11);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const UI_NOTIFICATION_CLASS = 'pdfSidebarNotification';
        const SidebarView = {
          UNKNOWN: -1,
          NONE: 0,
          THUMBS: 1,
          OUTLINE: 2,
          ATTACHMENTS: 3,
          LAYERS: 4
        };
        exports.SidebarView = SidebarView;

        const PDFSidebar =
            /*#__PURE__*/
            function () {
              function PDFSidebar(_ref) {
                const elements = _ref.elements,
                    pdfViewer = _ref.pdfViewer,
                    pdfThumbnailViewer = _ref.pdfThumbnailViewer,
                    eventBus = _ref.eventBus,
                    _ref$l10n = _ref.l10n,
                    l10n = _ref$l10n === void 0 ? _ui_utils.NullL10n : _ref$l10n,
                    _ref$disableNotificat = _ref.disableNotification,
                    disableNotification = _ref$disableNotificat === void 0 ? false : _ref$disableNotificat;

                _classCallCheck(this, PDFSidebar);

                this.isOpen = false;
                this.active = SidebarView.THUMBS;
                this.isInitialViewSet = false;
                this.onToggled = null;
                this.pdfViewer = pdfViewer;
                this.pdfThumbnailViewer = pdfThumbnailViewer;
                this.outerContainer = elements.outerContainer;
                this.viewerContainer = elements.viewerContainer;
                this.toggleButton = elements.toggleButton;
                this.thumbnailButton = elements.thumbnailButton;
                this.outlineButton = elements.outlineButton;
                this.attachmentsButton = elements.attachmentsButton;
                this.thumbnailView = elements.thumbnailView;
                this.outlineView = elements.outlineView;
                this.attachmentsView = elements.attachmentsView;
                this.eventBus = eventBus;
                this.l10n = l10n;
                this._disableNotification = disableNotification;

                this._addEventListeners();
              }

              _createClass(PDFSidebar, [{
                key: "reset",
                value: function reset() {
                  this.isInitialViewSet = false;

                  this._hideUINotification(null);

                  this.switchView(SidebarView.THUMBS);
                  this.outlineButton.disabled = false;
                  this.attachmentsButton.disabled = false;
                }
              }, {
                key: "setInitialView",
                value: function setInitialView() {
                  const view = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : SidebarView.NONE;

                  if (this.isInitialViewSet) {
                    return;
                  }

                  this.isInitialViewSet = true;

                  if (view === SidebarView.NONE || view === SidebarView.UNKNOWN) {
                    this._dispatchEvent();

                    return;
                  }

                  if (!this._switchView(view, true)) {
                    this._dispatchEvent();
                  }
                }
              }, {
                key: "switchView",
                value: function switchView(view) {
                  const forceOpen = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

                  this._switchView(view, forceOpen);
                }
              }, {
                key: "_switchView",
                value: function _switchView(view) {
                  const forceOpen = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  const isViewChanged = view !== this.active;
                  let shouldForceRendering = false;

                  switch (view) {
                    case SidebarView.NONE:
                      if (this.isOpen) {
                        this.close();
                        return true;
                      }

                      return false;

                    case SidebarView.THUMBS:
                      if (this.isOpen && isViewChanged) {
                        shouldForceRendering = true;
                      }

                      break;

                    case SidebarView.OUTLINE:
                      if (this.outlineButton.disabled) {
                        return false;
                      }

                      break;

                    case SidebarView.ATTACHMENTS:
                      if (this.attachmentsButton.disabled) {
                        return false;
                      }

                      break;

                    default:
                      console.error("PDFSidebar._switchView: \"".concat(view, "\" is not a valid view."));
                      return false;
                  }

                  this.active = view;
                  this.thumbnailButton.classList.toggle('toggled', view === SidebarView.THUMBS);
                  this.outlineButton.classList.toggle('toggled', view === SidebarView.OUTLINE);
                  this.attachmentsButton.classList.toggle('toggled', view === SidebarView.ATTACHMENTS);
                  this.thumbnailView.classList.toggle('hidden', view !== SidebarView.THUMBS);
                  this.outlineView.classList.toggle('hidden', view !== SidebarView.OUTLINE);
                  this.attachmentsView.classList.toggle('hidden', view !== SidebarView.ATTACHMENTS);

                  if (forceOpen && !this.isOpen) {
                    this.open();
                    return true;
                  }

                  if (shouldForceRendering) {
                    this._updateThumbnailViewer();

                    this._forceRendering();
                  }

                  if (isViewChanged) {
                    this._dispatchEvent();
                  }

                  this._hideUINotification(this.active);

                  return isViewChanged;
                }
              }, {
                key: "open",
                value: function open() {
                  if (this.isOpen) {
                    return;
                  }

                  this.isOpen = true;
                  this.toggleButton.classList.add('toggled');
                  this.outerContainer.classList.add('sidebarMoving', 'sidebarOpen');

                  if (this.active === SidebarView.THUMBS) {
                    this._updateThumbnailViewer();
                  }

                  this._forceRendering();

                  this._dispatchEvent();

                  this._hideUINotification(this.active);
                }
              }, {
                key: "close",
                value: function close() {
                  if (!this.isOpen) {
                    return;
                  }

                  this.isOpen = false;
                  this.toggleButton.classList.remove('toggled');
                  this.outerContainer.classList.add('sidebarMoving');
                  this.outerContainer.classList.remove('sidebarOpen');

                  this._forceRendering();

                  this._dispatchEvent();
                }
              }, {
                key: "toggle",
                value: function toggle() {
                  if (this.isOpen) {
                    this.close();
                  } else {
                    this.open();
                  }
                }
              }, {
                key: "_dispatchEvent",
                value: function _dispatchEvent() {
                  this.eventBus.dispatch('sidebarviewchanged', {
                    source: this,
                    view: this.visibleView
                  });
                }
              }, {
                key: "_forceRendering",
                value: function _forceRendering() {
                  if (this.onToggled) {
                    this.onToggled();
                  } else {
                    this.pdfViewer.forceRendering();
                    this.pdfThumbnailViewer.forceRendering();
                  }
                }
              }, {
                key: "_updateThumbnailViewer",
                value: function _updateThumbnailViewer() {
                  const pdfViewer = this.pdfViewer,
                      pdfThumbnailViewer = this.pdfThumbnailViewer;
                  const pagesCount = pdfViewer.pagesCount;

                  for (let pageIndex = 0; pageIndex < pagesCount; pageIndex++) {
                    const pageView = pdfViewer.getPageView(pageIndex);

                    if (pageView && pageView.renderingState === _pdf_rendering_queue.RenderingStates.FINISHED) {
                      const thumbnailView = pdfThumbnailViewer.getThumbnail(pageIndex);
                      thumbnailView.setImage(pageView);
                    }
                  }

                  pdfThumbnailViewer.scrollThumbnailIntoView(pdfViewer.currentPageNumber);
                }
              }, {
                key: "_showUINotification",
                value: function _showUINotification(view) {
                  const _this = this;

                  if (this._disableNotification) {
                    return;
                  }

                  this.l10n.get('toggle_sidebar_notification.title', null, 'Toggle Sidebar (document contains outline/attachments)').then(function (msg) {
                    _this.toggleButton.title = msg;
                  });

                  if (!this.isOpen) {
                    this.toggleButton.classList.add(UI_NOTIFICATION_CLASS);
                  } else if (view === this.active) {
                    return;
                  }

                  switch (view) {
                    case SidebarView.OUTLINE:
                      this.outlineButton.classList.add(UI_NOTIFICATION_CLASS);
                      break;

                    case SidebarView.ATTACHMENTS:
                      this.attachmentsButton.classList.add(UI_NOTIFICATION_CLASS);
                      break;
                  }
                }
              }, {
                key: "_hideUINotification",
                value: function _hideUINotification(view) {
                  const _this2 = this;

                  if (this._disableNotification) {
                    return;
                  }

                  const removeNotification = function removeNotification(view) {
                    switch (view) {
                      case SidebarView.OUTLINE:
                        _this2.outlineButton.classList.remove(UI_NOTIFICATION_CLASS);

                        break;

                      case SidebarView.ATTACHMENTS:
                        _this2.attachmentsButton.classList.remove(UI_NOTIFICATION_CLASS);

                        break;
                    }
                  };

                  if (!this.isOpen && view !== null) {
                    return;
                  }

                  this.toggleButton.classList.remove(UI_NOTIFICATION_CLASS);

                  if (view !== null) {
                    removeNotification(view);
                    return;
                  }

                  for (view in SidebarView) {
                    removeNotification(SidebarView[view]);
                  }

                  this.l10n.get('toggle_sidebar.title', null, 'Toggle Sidebar').then(function (msg) {
                    _this2.toggleButton.title = msg;
                  });
                }
              }, {
                key: "_addEventListeners",
                value: function _addEventListeners() {
                  const _this3 = this;

                  this.viewerContainer.addEventListener('transitionend', function (evt) {
                    if (evt.target === _this3.viewerContainer) {
                      _this3.outerContainer.classList.remove('sidebarMoving');
                    }
                  });
                  this.toggleButton.addEventListener('click', function () {
                    _this3.toggle();
                  });
                  this.thumbnailButton.addEventListener('click', function () {
                    _this3.switchView(SidebarView.THUMBS);
                  });
                  this.outlineButton.addEventListener('click', function () {
                    _this3.switchView(SidebarView.OUTLINE);
                  });
                  this.outlineButton.addEventListener('dblclick', function () {
                    _this3.eventBus.dispatch('toggleoutlinetree', {
                      source: _this3
                    });
                  });
                  this.attachmentsButton.addEventListener('click', function () {
                    _this3.switchView(SidebarView.ATTACHMENTS);
                  });
                  this.eventBus.on('outlineloaded', function (evt) {
                    const outlineCount = evt.outlineCount;
                    _this3.outlineButton.disabled = !outlineCount;

                    if (outlineCount) {
                      _this3._showUINotification(SidebarView.OUTLINE);
                    } else if (_this3.active === SidebarView.OUTLINE) {
                      _this3.switchView(SidebarView.THUMBS);
                    }
                  });
                  this.eventBus.on('attachmentsloaded', function (evt) {
                    if (evt.attachmentsCount) {
                      _this3.attachmentsButton.disabled = false;

                      _this3._showUINotification(SidebarView.ATTACHMENTS);

                      return;
                    }

                    Promise.resolve().then(function () {
                      if (_this3.attachmentsView.hasChildNodes()) {
                        return;
                      }

                      _this3.attachmentsButton.disabled = true;

                      if (_this3.active === SidebarView.ATTACHMENTS) {
                        _this3.switchView(SidebarView.THUMBS);
                      }
                    });
                  });
                  this.eventBus.on('presentationmodechanged', function (evt) {
                    if (!evt.active && !evt.switchInProgress && _this3.isThumbnailViewVisible) {
                      _this3._updateThumbnailViewer();
                    }
                  });
                }
              }, {
                key: "visibleView",
                get: function get() {
                  return this.isOpen ? this.active : SidebarView.NONE;
                }
              }, {
                key: "isThumbnailViewVisible",
                get: function get() {
                  return this.isOpen && this.active === SidebarView.THUMBS;
                }
              }, {
                key: "isOutlineViewVisible",
                get: function get() {
                  return this.isOpen && this.active === SidebarView.OUTLINE;
                }
              }, {
                key: "isAttachmentsViewVisible",
                get: function get() {
                  return this.isOpen && this.active === SidebarView.ATTACHMENTS;
                }
              }]);

              return PDFSidebar;
            }();

        exports.PDFSidebar = PDFSidebar;

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.OverlayManager = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const OverlayManager =
            /*#__PURE__*/
            function () {
              function OverlayManager() {
                _classCallCheck(this, OverlayManager);

                this._overlays = {};
                this._active = null;
                this._keyDownBound = this._keyDown.bind(this);
              }

              _createClass(OverlayManager, [{
                key: "register",
                value: function () {
                  const _register = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee(name, element) {
                        let callerCloseMethod,
                            canForceClose,
                            container;
                        const _args = arguments;
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                callerCloseMethod = _args.length > 2 && _args[2] !== undefined ? _args[2] : null;
                                canForceClose = _args.length > 3 && _args[3] !== undefined ? _args[3] : false;

                                if (!(!name || !element || !(container = element.parentNode))) {
                                  _context.next = 6;
                                  break;
                                }

                                throw new Error('Not enough parameters.');

                              case 6:
                                if (!this._overlays[name]) {
                                  _context.next = 8;
                                  break;
                                }

                                throw new Error('The overlay is already registered.');

                              case 8:
                                this._overlays[name] = {
                                  element: element,
                                  container: container,
                                  callerCloseMethod: callerCloseMethod,
                                  canForceClose: canForceClose
                                };

                              case 9:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee, this);
                      }));

                  function register(_x, _x2) {
                    return _register.apply(this, arguments);
                  }

                  return register;
                }()
              }, {
                key: "unregister",
                value: function () {
                  const _unregister = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2(name) {
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                if (this._overlays[name]) {
                                  _context2.next = 4;
                                  break;
                                }

                                throw new Error('The overlay does not exist.');

                              case 4:
                                if (!(this._active === name)) {
                                  _context2.next = 6;
                                  break;
                                }

                                throw new Error('The overlay cannot be removed while it is active.');

                              case 6:
                                delete this._overlays[name];

                              case 7:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2, this);
                      }));

                  function unregister(_x3) {
                    return _unregister.apply(this, arguments);
                  }

                  return unregister;
                }()
              }, {
                key: "open",
                value: function () {
                  const _open = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee3(name) {
                        return _regenerator["default"].wrap(function _callee3$(_context3) {
                          while (1) {
                            switch (_context3.prev = _context3.next) {
                              case 0:
                                if (this._overlays[name]) {
                                  _context3.next = 4;
                                  break;
                                }

                                throw new Error('The overlay does not exist.');

                              case 4:
                                if (!this._active) {
                                  _context3.next = 14;
                                  break;
                                }

                                if (!this._overlays[name].canForceClose) {
                                  _context3.next = 9;
                                  break;
                                }

                                this._closeThroughCaller();

                                _context3.next = 14;
                                break;

                              case 9:
                                if (!(this._active === name)) {
                                  _context3.next = 13;
                                  break;
                                }

                                throw new Error('The overlay is already active.');

                              case 13:
                                throw new Error('Another overlay is currently active.');

                              case 14:
                                this._active = name;

                                this._overlays[this._active].element.classList.remove('hidden');

                                this._overlays[this._active].container.classList.remove('hidden');

                                window.addEventListener('keydown', this._keyDownBound);

                              case 18:
                              case "end":
                                return _context3.stop();
                            }
                          }
                        }, _callee3, this);
                      }));

                  function open(_x4) {
                    return _open.apply(this, arguments);
                  }

                  return open;
                }()
              }, {
                key: "close",
                value: function () {
                  const _close = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee4(name) {
                        return _regenerator["default"].wrap(function _callee4$(_context4) {
                          while (1) {
                            switch (_context4.prev = _context4.next) {
                              case 0:
                                if (this._overlays[name]) {
                                  _context4.next = 4;
                                  break;
                                }

                                throw new Error('The overlay does not exist.');

                              case 4:
                                if (this._active) {
                                  _context4.next = 8;
                                  break;
                                }

                                throw new Error('The overlay is currently not active.');

                              case 8:
                                if (!(this._active !== name)) {
                                  _context4.next = 10;
                                  break;
                                }

                                throw new Error('Another overlay is currently active.');

                              case 10:
                                this._overlays[this._active].container.classList.add('hidden');

                                this._overlays[this._active].element.classList.add('hidden');

                                this._active = null;
                                window.removeEventListener('keydown', this._keyDownBound);

                              case 14:
                              case "end":
                                return _context4.stop();
                            }
                          }
                        }, _callee4, this);
                      }));

                  function close(_x5) {
                    return _close.apply(this, arguments);
                  }

                  return close;
                }()
              }, {
                key: "_keyDown",
                value: function _keyDown(evt) {
                  if (this._active && evt.keyCode === 27) {
                    this._closeThroughCaller();

                    evt.preventDefault();
                  }
                }
              }, {
                key: "_closeThroughCaller",
                value: function _closeThroughCaller() {
                  if (this._overlays[this._active].callerCloseMethod) {
                    this._overlays[this._active].callerCloseMethod();
                  }

                  if (this._active) {
                    this.close(this._active);
                  }
                }
              }, {
                key: "active",
                get: function get() {
                  return this._active;
                }
              }]);

              return OverlayManager;
            }();

        exports.OverlayManager = OverlayManager;

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PasswordPrompt = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdfjsLib = __webpack_require__(7);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const PasswordPrompt =
            /*#__PURE__*/
            function () {
              function PasswordPrompt(options, overlayManager) {
                const _this = this;

                const l10n = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : _ui_utils.NullL10n;

                _classCallCheck(this, PasswordPrompt);

                this.overlayName = options.overlayName;
                this.container = options.container;
                this.label = options.label;
                this.input = options.input;
                this.submitButton = options.submitButton;
                this.cancelButton = options.cancelButton;
                this.overlayManager = overlayManager;
                this.l10n = l10n;
                this.updateCallback = null;
                this.reason = null;
                this.submitButton.addEventListener('click', this.verify.bind(this));
                this.cancelButton.addEventListener('click', this.close.bind(this));
                this.input.addEventListener('keydown', function (e) {
                  if (e.keyCode === 13) {
                    _this.verify();
                  }
                });
                this.overlayManager.register(this.overlayName, this.container, this.close.bind(this), true);
              }

              _createClass(PasswordPrompt, [{
                key: "open",
                value: function open() {
                  const _this2 = this;

                  this.overlayManager.open(this.overlayName).then(function () {
                    _this2.input.focus();

                    let promptString;

                    if (_this2.reason === _pdfjsLib.PasswordResponses.INCORRECT_PASSWORD) {
                      promptString = _this2.l10n.get('password_invalid', null, 'Invalid password. Please try again.');
                    } else {
                      promptString = _this2.l10n.get('password_label', null, 'Enter the password to open this PDF file.');
                    }

                    promptString.then(function (msg) {
                      _this2.label.textContent = msg;
                    });
                  });
                }
              }, {
                key: "close",
                value: function close() {
                  const _this3 = this;

                  this.overlayManager.close(this.overlayName).then(function () {
                    _this3.input.value = '';
                  });
                }
              }, {
                key: "verify",
                value: function verify() {
                  const password = this.input.value;

                  if (password && password.length > 0) {
                    this.close();
                    this.updateCallback(password);
                  }
                }
              }, {
                key: "setUpdateCallback",
                value: function setUpdateCallback(updateCallback, reason) {
                  this.updateCallback = updateCallback;
                  this.reason = reason;
                }
              }]);

              return PasswordPrompt;
            }();

        exports.PasswordPrompt = PasswordPrompt;

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFAttachmentViewer = void 0;

        const _pdfjsLib = __webpack_require__(7);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const PDFAttachmentViewer =
            /*#__PURE__*/
            function () {
              function PDFAttachmentViewer(_ref) {
                const container = _ref.container,
                    eventBus = _ref.eventBus,
                    downloadManager = _ref.downloadManager;

                _classCallCheck(this, PDFAttachmentViewer);

                this.container = container;
                this.eventBus = eventBus;
                this.downloadManager = downloadManager;
                this.reset();
                this.eventBus.on('fileattachmentannotation', this._appendAttachment.bind(this));
              }

              _createClass(PDFAttachmentViewer, [{
                key: "reset",
                value: function reset() {
                  const keepRenderedCapability = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
                  this.attachments = null;
                  this.container.textContent = '';

                  if (!keepRenderedCapability) {
                    this._renderedCapability = (0, _pdfjsLib.createPromiseCapability)();
                  }
                }
              }, {
                key: "_dispatchEvent",
                value: function _dispatchEvent(attachmentsCount) {
                  this._renderedCapability.resolve();

                  this.eventBus.dispatch('attachmentsloaded', {
                    source: this,
                    attachmentsCount: attachmentsCount
                  });
                }
              }, {
                key: "_bindPdfLink",
                value: function _bindPdfLink(button, content, filename) {
                  if (this.downloadManager.disableCreateObjectURL) {
                    throw new Error('bindPdfLink: Unsupported "disableCreateObjectURL" value.');
                  }

                  let blobUrl;

                  button.onclick = function () {
                    if (!blobUrl) {
                      blobUrl = (0, _pdfjsLib.createObjectURL)(content, 'application/pdf');
                    }

                    let viewerUrl;
                    viewerUrl = '?file=' + encodeURIComponent(blobUrl + '#' + filename);
                    window.open(viewerUrl);
                    return false;
                  };
                }
              }, {
                key: "_bindLink",
                value: function _bindLink(button, content, filename) {
                  const _this = this;

                  button.onclick = function () {
                    _this.downloadManager.downloadData(content, filename, '');

                    return false;
                  };
                }
              }, {
                key: "render",
                value: function render(_ref2) {
                  const attachments = _ref2.attachments,
                      _ref2$keepRenderedCap = _ref2.keepRenderedCapability,
                      keepRenderedCapability = _ref2$keepRenderedCap === void 0 ? false : _ref2$keepRenderedCap;
                  let attachmentsCount = 0;

                  if (this.attachments) {
                    this.reset(keepRenderedCapability === true);
                  }

                  this.attachments = attachments || null;

                  if (!attachments) {
                    this._dispatchEvent(attachmentsCount);

                    return;
                  }

                  const names = Object.keys(attachments).sort(function (a, b) {
                    return a.toLowerCase().localeCompare(b.toLowerCase());
                  });
                  attachmentsCount = names.length;

                  for (let i = 0; i < attachmentsCount; i++) {
                    const item = attachments[names[i]];
                    const filename = (0, _pdfjsLib.removeNullCharacters)((0, _pdfjsLib.getFilenameFromUrl)(item.filename));
                    const div = document.createElement('div');
                    div.className = 'attachmentsItem';
                    const button = document.createElement('button');
                    button.textContent = filename;

                    if (/\.pdf$/i.test(filename) && !this.downloadManager.disableCreateObjectURL) {
                      this._bindPdfLink(button, item.content, filename);
                    } else {
                      this._bindLink(button, item.content, filename);
                    }

                    div.appendChild(button);
                    this.container.appendChild(div);
                  }

                  this._dispatchEvent(attachmentsCount);
                }
              }, {
                key: "_appendAttachment",
                value: function _appendAttachment(_ref3) {
                  const _this2 = this;

                  const id = _ref3.id,
                      filename = _ref3.filename,
                      content = _ref3.content;

                  this._renderedCapability.promise.then(function () {
                    let attachments = _this2.attachments;

                    if (!attachments) {
                      attachments = Object.create(null);
                    } else {
                      for (let name in attachments) {
                        if (id === name) {
                          return;
                        }
                      }
                    }

                    attachments[id] = {
                      filename: filename,
                      content: content
                    };

                    _this2.render({
                      attachments: attachments,
                      keepRenderedCapability: true
                    });
                  });
                }
              }]);

              return PDFAttachmentViewer;
            }();

        exports.PDFAttachmentViewer = PDFAttachmentViewer;

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFDocumentProperties = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        const _pdfjsLib = __webpack_require__(7);

        const _ui_utils = __webpack_require__(5);

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) { return; }
  const _arr = [];
  let _n = true;
  let _d = false;
  let _e = undefined;
  try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const DEFAULT_FIELD_CONTENT = '-';
        const NON_METRIC_LOCALES = ['en-us', 'en-lr', 'my'];
        const US_PAGE_NAMES = {
          '8.5x11': 'Letter',
          '8.5x14': 'Legal'
        };
        const METRIC_PAGE_NAMES = {
          '297x420': 'A3',
          '210x297': 'A4'
        };

        function getPageName(size, isPortrait, pageNames) {
  const width = isPortrait ? size.width : size.height;
  const height = isPortrait ? size.height : size.width;
  return pageNames["".concat(width, "x").concat(height)];
}

        const PDFDocumentProperties =
            /*#__PURE__*/
            function () {
              function PDFDocumentProperties(_ref, overlayManager, eventBus) {
                const _this = this;

                const overlayName = _ref.overlayName,
                    fields = _ref.fields,
                    container = _ref.container,
                    closeButton = _ref.closeButton;
                const l10n = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : _ui_utils.NullL10n;

                _classCallCheck(this, PDFDocumentProperties);

                this.overlayName = overlayName;
                this.fields = fields;
                this.container = container;
                this.overlayManager = overlayManager;
                this.l10n = l10n;

                this._reset();

                if (closeButton) {
                  closeButton.addEventListener('click', this.close.bind(this));
                }

                this.overlayManager.register(this.overlayName, this.container, this.close.bind(this));

                if (eventBus) {
                  eventBus.on('pagechanging', function (evt) {
                    _this._currentPageNumber = evt.pageNumber;
                  });
                  eventBus.on('rotationchanging', function (evt) {
                    _this._pagesRotation = evt.pagesRotation;
                  });
                }

                this._isNonMetricLocale = true;
                l10n.getLanguage().then(function (locale) {
                  _this._isNonMetricLocale = NON_METRIC_LOCALES.includes(locale);
                });
              }

              _createClass(PDFDocumentProperties, [{
                key: "open",
                value: function open() {
                  const _this2 = this;

                  const freezeFieldData = function freezeFieldData(data) {
                    Object.defineProperty(_this2, 'fieldData', {
                      value: Object.freeze(data),
                      writable: false,
                      enumerable: true,
                      configurable: true
                    });
                  };

                  Promise.all([this.overlayManager.open(this.overlayName), this._dataAvailableCapability.promise]).then(function () {
                    const currentPageNumber = _this2._currentPageNumber;
                    const pagesRotation = _this2._pagesRotation;

                    if (_this2.fieldData && currentPageNumber === _this2.fieldData['_currentPageNumber'] && pagesRotation === _this2.fieldData['_pagesRotation']) {
                      _this2._updateUI();

                      return;
                    }

                    _this2.pdfDocument.getMetadata().then(function (_ref2) {
                      const info = _ref2.info,
                          metadata = _ref2.metadata,
                          contentDispositionFilename = _ref2.contentDispositionFilename;
                      return Promise.all([info, metadata, contentDispositionFilename || (0, _ui_utils.getPDFFileNameFromURL)(_this2.url || ''), _this2._parseFileSize(_this2.maybeFileSize), _this2._parseDate(info.CreationDate), _this2._parseDate(info.ModDate), _this2.pdfDocument.getPage(currentPageNumber).then(function (pdfPage) {
                        return _this2._parsePageSize((0, _ui_utils.getPageSizeInches)(pdfPage), pagesRotation);
                      }), _this2._parseLinearization(info.IsLinearized)]);
                    }).then(function (_ref3) {
                      const _ref4 = _slicedToArray(_ref3, 8),
                          info = _ref4[0],
                          metadata = _ref4[1],
                          fileName = _ref4[2],
                          fileSize = _ref4[3],
                          creationDate = _ref4[4],
                          modDate = _ref4[5],
                          pageSize = _ref4[6],
                          isLinearized = _ref4[7];

                      freezeFieldData({
                        'fileName': fileName,
                        'fileSize': fileSize,
                        'title': info.Title,
                        'author': info.Author,
                        'subject': info.Subject,
                        'keywords': info.Keywords,
                        'creationDate': creationDate,
                        'modificationDate': modDate,
                        'creator': info.Creator,
                        'producer': info.Producer,
                        'version': info.PDFFormatVersion,
                        'pageCount': _this2.pdfDocument.numPages,
                        'pageSize': pageSize,
                        'linearized': isLinearized,
                        '_currentPageNumber': currentPageNumber,
                        '_pagesRotation': pagesRotation
                      });

                      _this2._updateUI();

                      return _this2.pdfDocument.getDownloadInfo();
                    }).then(function (_ref5) {
                      const length = _ref5.length;
                      _this2.maybeFileSize = length;
                      return _this2._parseFileSize(length);
                    }).then(function (fileSize) {
                      if (fileSize === _this2.fieldData['fileSize']) {
                        return;
                      }

                      const data = Object.assign(Object.create(null), _this2.fieldData);
                      data['fileSize'] = fileSize;
                      freezeFieldData(data);

                      _this2._updateUI();
                    });
                  });
                }
              }, {
                key: "close",
                value: function close() {
                  this.overlayManager.close(this.overlayName);
                }
              }, {
                key: "setDocument",
                value: function setDocument(pdfDocument) {
                  const url = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;

                  if (this.pdfDocument) {
                    this._reset();

                    this._updateUI(true);
                  }

                  if (!pdfDocument) {
                    return;
                  }

                  this.pdfDocument = pdfDocument;
                  this.url = url;

                  this._dataAvailableCapability.resolve();
                }
              }, {
                key: "setFileSize",
                value: function setFileSize(fileSize) {
                  if (Number.isInteger(fileSize) && fileSize > 0) {
                    this.maybeFileSize = fileSize;
                  }
                }
              }, {
                key: "_reset",
                value: function _reset() {
                  this.pdfDocument = null;
                  this.url = null;
                  this.maybeFileSize = 0;
                  delete this.fieldData;
                  this._dataAvailableCapability = (0, _pdfjsLib.createPromiseCapability)();
                  this._currentPageNumber = 1;
                  this._pagesRotation = 0;
                }
              }, {
                key: "_updateUI",
                value: function _updateUI() {
                  const reset = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

                  if (reset || !this.fieldData) {
                    for (let id in this.fields) {
                      this.fields[id].textContent = DEFAULT_FIELD_CONTENT;
                    }

                    return;
                  }

                  if (this.overlayManager.active !== this.overlayName) {
                    return;
                  }

                  for (let _id in this.fields) {
                    const content = this.fieldData[_id];
                    this.fields[_id].textContent = content || content === 0 ? content : DEFAULT_FIELD_CONTENT;
                  }
                }
              }, {
                key: "_parseFileSize",
                value: function () {
                  const _parseFileSize2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee() {
                        let fileSize,
                            kb;
                        const _args = arguments;
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                fileSize = _args.length > 0 && _args[0] !== undefined ? _args[0] : 0;
                                kb = fileSize / 1024;

                                if (kb) {
                                  _context.next = 6;
                                  break;
                                }

                                return _context.abrupt("return", undefined);

                              case 6:
                                if (!(kb < 1024)) {
                                  _context.next = 8;
                                  break;
                                }

                                return _context.abrupt("return", this.l10n.get('document_properties_kb', {
                                  size_kb: (+kb.toPrecision(3)).toLocaleString(),
                                  size_b: fileSize.toLocaleString()
                                }, '{{size_kb}} KB ({{size_b}} bytes)'));

                              case 8:
                                return _context.abrupt("return", this.l10n.get('document_properties_mb', {
                                  size_mb: (+(kb / 1024).toPrecision(3)).toLocaleString(),
                                  size_b: fileSize.toLocaleString()
                                }, '{{size_mb}} MB ({{size_b}} bytes)'));

                              case 9:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee, this);
                      }));

                  function _parseFileSize() {
                    return _parseFileSize2.apply(this, arguments);
                  }

                  return _parseFileSize;
                }()
              }, {
                key: "_parsePageSize",
                value: function () {
                  const _parsePageSize2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2(pageSizeInches, pagesRotation) {
                        const _this3 = this;

                        let isPortrait, sizeInches, sizeMillimeters, pageName, name, exactMillimeters, intMillimeters;
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                if (pageSizeInches) {
                                  _context2.next = 2;
                                  break;
                                }

                                return _context2.abrupt("return", undefined);

                              case 2:
                                if (pagesRotation % 180 !== 0) {
                                  pageSizeInches = {
                                    width: pageSizeInches.height,
                                    height: pageSizeInches.width
                                  };
                                }

                                isPortrait = (0, _ui_utils.isPortraitOrientation)(pageSizeInches);
                                sizeInches = {
                                  width: Math.round(pageSizeInches.width * 100) / 100,
                                  height: Math.round(pageSizeInches.height * 100) / 100
                                };
                                sizeMillimeters = {
                                  width: Math.round(pageSizeInches.width * 25.4 * 10) / 10,
                                  height: Math.round(pageSizeInches.height * 25.4 * 10) / 10
                                };
                                pageName = null;
                                name = getPageName(sizeInches, isPortrait, US_PAGE_NAMES) || getPageName(sizeMillimeters, isPortrait, METRIC_PAGE_NAMES);

                                if (!name && !(Number.isInteger(sizeMillimeters.width) && Number.isInteger(sizeMillimeters.height))) {
                                  exactMillimeters = {
                                    width: pageSizeInches.width * 25.4,
                                    height: pageSizeInches.height * 25.4
                                  };
                                  intMillimeters = {
                                    width: Math.round(sizeMillimeters.width),
                                    height: Math.round(sizeMillimeters.height)
                                  };

                                  if (Math.abs(exactMillimeters.width - intMillimeters.width) < 0.1 && Math.abs(exactMillimeters.height - intMillimeters.height) < 0.1) {
                                    name = getPageName(intMillimeters, isPortrait, METRIC_PAGE_NAMES);

                                    if (name) {
                                      sizeInches = {
                                        width: Math.round(intMillimeters.width / 25.4 * 100) / 100,
                                        height: Math.round(intMillimeters.height / 25.4 * 100) / 100
                                      };
                                      sizeMillimeters = intMillimeters;
                                    }
                                  }
                                }

                                if (name) {
                                  pageName = this.l10n.get('document_properties_page_size_name_' + name.toLowerCase(), null, name);
                                }

                                return _context2.abrupt("return", Promise.all([this._isNonMetricLocale ? sizeInches : sizeMillimeters, this.l10n.get('document_properties_page_size_unit_' + (this._isNonMetricLocale ? 'inches' : 'millimeters'), null, this._isNonMetricLocale ? 'in' : 'mm'), pageName, this.l10n.get('document_properties_page_size_orientation_' + (isPortrait ? 'portrait' : 'landscape'), null, isPortrait ? 'portrait' : 'landscape')]).then(function (_ref6) {
                                  const _ref7 = _slicedToArray(_ref6, 4),
                                      _ref7$ = _ref7[0],
                                      width = _ref7$.width,
                                      height = _ref7$.height,
                                      unit = _ref7[1],
                                      name = _ref7[2],
                                      orientation = _ref7[3];

                                  return _this3.l10n.get('document_properties_page_size_dimension_' + (name ? 'name_' : '') + 'string', {
                                    width: width.toLocaleString(),
                                    height: height.toLocaleString(),
                                    unit: unit,
                                    name: name,
                                    orientation: orientation
                                  }, '{{width}} × {{height}} {{unit}} (' + (name ? '{{name}}, ' : '') + '{{orientation}})');
                                }));

                              case 11:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2, this);
                      }));

                  function _parsePageSize(_x, _x2) {
                    return _parsePageSize2.apply(this, arguments);
                  }

                  return _parsePageSize;
                }()
              }, {
                key: "_parseDate",
                value: function () {
                  const _parseDate2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee3(inputDate) {
                        let dateObject;
                        return _regenerator["default"].wrap(function _callee3$(_context3) {
                          while (1) {
                            switch (_context3.prev = _context3.next) {
                              case 0:
                                dateObject = _pdfjsLib.PDFDateString.toDateObject(inputDate);

                                if (dateObject) {
                                  _context3.next = 3;
                                  break;
                                }

                                return _context3.abrupt("return", undefined);

                              case 3:
                                return _context3.abrupt("return", this.l10n.get('document_properties_date_string', {
                                  date: dateObject.toLocaleDateString(),
                                  time: dateObject.toLocaleTimeString()
                                }, '{{date}}, {{time}}'));

                              case 4:
                              case "end":
                                return _context3.stop();
                            }
                          }
                        }, _callee3, this);
                      }));

                  function _parseDate(_x3) {
                    return _parseDate2.apply(this, arguments);
                  }

                  return _parseDate;
                }()
              }, {
                key: "_parseLinearization",
                value: function _parseLinearization(isLinearized) {
                  return this.l10n.get('document_properties_linearized_' + (isLinearized ? 'yes' : 'no'), null, isLinearized ? 'Yes' : 'No');
                }
              }]);

              return PDFDocumentProperties;
            }();

        exports.PDFDocumentProperties = PDFDocumentProperties;

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFFindBar = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdf_find_controller = __webpack_require__(18);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const MATCHES_COUNT_LIMIT = 1000;

        const PDFFindBar =
            /*#__PURE__*/
            function () {
              function PDFFindBar(options) {
                const _this = this;

                const eventBus = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : (0, _ui_utils.getGlobalEventBus)();
                const l10n = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : _ui_utils.NullL10n;

                _classCallCheck(this, PDFFindBar);

                this.opened = false;
                this.bar = options.bar || null;
                this.toggleButton = options.toggleButton || null;
                this.findField = options.findField || null;
                this.highlightAll = options.highlightAllCheckbox || null;
                this.caseSensitive = options.caseSensitiveCheckbox || null;
                this.entireWord = options.entireWordCheckbox || null;
                this.findMsg = options.findMsg || null;
                this.findResultsCount = options.findResultsCount || null;
                this.findPreviousButton = options.findPreviousButton || null;
                this.findNextButton = options.findNextButton || null;
                this.eventBus = eventBus;
                this.l10n = l10n;
                this.toggleButton.addEventListener('click', function () {
                  _this.toggle();
                });
                this.findField.addEventListener('input', function () {
                  _this.dispatchEvent('');
                });
                this.bar.addEventListener('keydown', function (e) {
                  switch (e.keyCode) {
                    case 13:
                      if (e.target === _this.findField) {
                        _this.dispatchEvent('again', e.shiftKey);
                      }

                      break;

                    case 27:
                      _this.close();

                      break;
                  }
                });
                this.findPreviousButton.addEventListener('click', function () {
                  _this.dispatchEvent('again', true);
                });
                this.findNextButton.addEventListener('click', function () {
                  _this.dispatchEvent('again', false);
                });
                this.highlightAll.addEventListener('click', function () {
                  _this.dispatchEvent('highlightallchange');
                });
                this.caseSensitive.addEventListener('click', function () {
                  _this.dispatchEvent('casesensitivitychange');
                });
                this.entireWord.addEventListener('click', function () {
                  _this.dispatchEvent('entirewordchange');
                });
                this.eventBus.on('resize', this._adjustWidth.bind(this));
              }

              _createClass(PDFFindBar, [{
                key: "reset",
                value: function reset() {
                  this.updateUIState();
                }
              }, {
                key: "dispatchEvent",
                value: function dispatchEvent(type, findPrev) {
                  this.eventBus.dispatch('find', {
                    source: this,
                    type: type,
                    query: this.findField.value,
                    phraseSearch: true,
                    caseSensitive: this.caseSensitive.checked,
                    entireWord: this.entireWord.checked,
                    highlightAll: this.highlightAll.checked,
                    findPrevious: findPrev
                  });
                }
              }, {
                key: "updateUIState",
                value: function updateUIState(state, previous, matchesCount) {
                  const _this2 = this;

                  let notFound = false;
                  let findMsg = '';
                  let status = '';

                  switch (state) {
                    case _pdf_find_controller.FindState.FOUND:
                      break;

                    case _pdf_find_controller.FindState.PENDING:
                      status = 'pending';
                      break;

                    case _pdf_find_controller.FindState.NOT_FOUND:
                      findMsg = this.l10n.get('find_not_found', null, 'Phrase not found');
                      notFound = true;
                      break;

                    case _pdf_find_controller.FindState.WRAPPED:
                      if (previous) {
                        findMsg = this.l10n.get('find_reached_top', null, 'Reached top of document, continued from bottom');
                      } else {
                        findMsg = this.l10n.get('find_reached_bottom', null, 'Reached end of document, continued from top');
                      }

                      break;
                  }

                  this.findField.classList.toggle('notFound', notFound);
                  this.findField.setAttribute('data-status', status);
                  Promise.resolve(findMsg).then(function (msg) {
                    _this2.findMsg.textContent = msg;

                    _this2._adjustWidth();
                  });
                  this.updateResultsCount(matchesCount);
                }
              }, {
                key: "updateResultsCount",
                value: function updateResultsCount() {
                  const _this3 = this;

                  const _ref = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                      _ref$current = _ref.current,
                      current = _ref$current === void 0 ? 0 : _ref$current,
                      _ref$total = _ref.total,
                      total = _ref$total === void 0 ? 0 : _ref$total;

                  if (!this.findResultsCount) {
                    return;
                  }

                  let matchesCountMsg = '';
                  const limit = MATCHES_COUNT_LIMIT;

                  if (total > 0) {
                    if (total > limit) {
                      matchesCountMsg = this.l10n.get('find_match_count_limit', {
                        limit: limit
                      }, 'More than {{limit}} match' + (limit !== 1 ? 'es' : ''));
                    } else {
                      matchesCountMsg = this.l10n.get('find_match_count', {
                        current: current,
                        total: total
                      }, '{{current}} of {{total}} match' + (total !== 1 ? 'es' : ''));
                    }
                  }

                  Promise.resolve(matchesCountMsg).then(function (msg) {
                    _this3.findResultsCount.textContent = msg;

                    _this3.findResultsCount.classList.toggle('hidden', !total);

                    _this3._adjustWidth();
                  });
                }
              }, {
                key: "open",
                value: function open() {
                  if (!this.opened) {
                    this.opened = true;
                    this.toggleButton.classList.add('toggled');
                    this.bar.classList.remove('hidden');
                  }

                  this.findField.select();
                  this.findField.focus();

                  this._adjustWidth();
                }
              }, {
                key: "close",
                value: function close() {
                  if (!this.opened) {
                    return;
                  }

                  this.opened = false;
                  this.toggleButton.classList.remove('toggled');
                  this.bar.classList.add('hidden');
                  this.eventBus.dispatch('findbarclose', {
                    source: this
                  });
                }
              }, {
                key: "toggle",
                value: function toggle() {
                  if (this.opened) {
                    this.close();
                  } else {
                    this.open();
                  }
                }
              }, {
                key: "_adjustWidth",
                value: function _adjustWidth() {
                  if (!this.opened) {
                    return;
                  }

                  this.bar.classList.remove('wrapContainers');
                  const findbarHeight = this.bar.clientHeight;
                  const inputContainerHeight = this.bar.firstElementChild.clientHeight;

                  if (findbarHeight > inputContainerHeight) {
                    this.bar.classList.add('wrapContainers');
                  }
                }
              }]);

              return PDFFindBar;
            }();

        exports.PDFFindBar = PDFFindBar;

/***/ }),
/* 18 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFFindController = exports.FindState = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdfjsLib = __webpack_require__(7);

        const _pdf_find_utils = __webpack_require__(19);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const FindState = {
          FOUND: 0,
          NOT_FOUND: 1,
          WRAPPED: 2,
          PENDING: 3
        };
        exports.FindState = FindState;
        const FIND_TIMEOUT = 250;
        const MATCH_SCROLL_OFFSET_TOP = -50;
        const MATCH_SCROLL_OFFSET_LEFT = -400;
        const CHARACTERS_TO_NORMALIZE = {
          "\u2018": '\'',
          "\u2019": '\'',
          "\u201A": '\'',
          "\u201B": '\'',
          "\u201C": '"',
          "\u201D": '"',
          "\u201E": '"',
          "\u201F": '"',
          "\xBC": '1/4',
          "\xBD": '1/2',
          "\xBE": '3/4'
        };
        let normalizationRegex = null;

        function normalize(text) {
  if (!normalizationRegex) {
    const replace = Object.keys(CHARACTERS_TO_NORMALIZE).join('');
    normalizationRegex = new RegExp("[".concat(replace, "]"), 'g');
  }

  return text.replace(normalizationRegex, function (ch) {
    return CHARACTERS_TO_NORMALIZE[ch];
  });
}

        const PDFFindController =
            /*#__PURE__*/
            function () {
              function PDFFindController(_ref) {
                const linkService = _ref.linkService,
                    _ref$eventBus = _ref.eventBus,
                    eventBus = _ref$eventBus === void 0 ? (0, _ui_utils.getGlobalEventBus)() : _ref$eventBus;

                _classCallCheck(this, PDFFindController);

                this._linkService = linkService;
                this._eventBus = eventBus;

                this._reset();

                eventBus.on('findbarclose', this._onFindBarClose.bind(this));
              }

              _createClass(PDFFindController, [{
                key: "setDocument",
                value: function setDocument(pdfDocument) {
                  if (this._pdfDocument) {
                    this._reset();
                  }

                  if (!pdfDocument) {
                    return;
                  }

                  this._pdfDocument = pdfDocument;

                  this._firstPageCapability.resolve();
                }
              }, {
                key: "executeCommand",
                value: function executeCommand(cmd, state) {
                  const _this = this;

                  if (!state) {
                    return;
                  }

                  const pdfDocument = this._pdfDocument;

                  if (this._state === null || this._shouldDirtyMatch(cmd, state)) {
                    this._dirtyMatch = true;
                  }

                  this._state = state;

                  if (cmd !== 'findhighlightallchange') {
                    this._updateUIState(FindState.PENDING);
                  }

                  this._firstPageCapability.promise.then(function () {
                    if (!_this._pdfDocument || pdfDocument && _this._pdfDocument !== pdfDocument) {
                      return;
                    }

                    _this._extractText();

                    const findbarClosed = !_this._highlightMatches;
                    const pendingTimeout = !!_this._findTimeout;

                    if (_this._findTimeout) {
                      clearTimeout(_this._findTimeout);
                      _this._findTimeout = null;
                    }

                    if (cmd === 'find') {
                      _this._findTimeout = setTimeout(function () {
                        _this._nextMatch();

                        _this._findTimeout = null;
                      }, FIND_TIMEOUT);
                    } else if (_this._dirtyMatch) {
                      _this._nextMatch();
                    } else if (cmd === 'findagain') {
                      _this._nextMatch();

                      if (findbarClosed && _this._state.highlightAll) {
                        _this._updateAllPages();
                      }
                    } else if (cmd === 'findhighlightallchange') {
                      if (pendingTimeout) {
                        _this._nextMatch();
                      } else {
                        _this._highlightMatches = true;
                      }

                      _this._updateAllPages();
                    } else {
                      _this._nextMatch();
                    }
                  });
                }
              }, {
                key: "scrollMatchIntoView",
                value: function scrollMatchIntoView(_ref2) {
                  const _ref2$element = _ref2.element,
                      element = _ref2$element === void 0 ? null : _ref2$element,
                      _ref2$pageIndex = _ref2.pageIndex,
                      pageIndex = _ref2$pageIndex === void 0 ? -1 : _ref2$pageIndex,
                      _ref2$matchIndex = _ref2.matchIndex,
                      matchIndex = _ref2$matchIndex === void 0 ? -1 : _ref2$matchIndex;

                  if (!this._scrollMatches || !element) {
                    return;
                  } else if (matchIndex === -1 || matchIndex !== this._selected.matchIdx) {
                    return;
                  } else if (pageIndex === -1 || pageIndex !== this._selected.pageIdx) {
                    return;
                  }

                  this._scrollMatches = false;
                  const spot = {
                    top: MATCH_SCROLL_OFFSET_TOP,
                    left: MATCH_SCROLL_OFFSET_LEFT
                  };
                  (0, _ui_utils.scrollIntoView)(element, spot, true);
                }
              }, {
                key: "_reset",
                value: function _reset() {
                  this._highlightMatches = false;
                  this._scrollMatches = false;
                  this._pdfDocument = null;
                  this._pageMatches = [];
                  this._pageMatchesLength = [];
                  this._state = null;
                  this._selected = {
                    pageIdx: -1,
                    matchIdx: -1
                  };
                  this._offset = {
                    pageIdx: null,
                    matchIdx: null,
                    wrapped: false
                  };
                  this._extractTextPromises = [];
                  this._pageContents = [];
                  this._matchesCountTotal = 0;
                  this._pagesToSearch = null;
                  this._pendingFindMatches = Object.create(null);
                  this._resumePageIdx = null;
                  this._dirtyMatch = false;
                  clearTimeout(this._findTimeout);
                  this._findTimeout = null;
                  this._firstPageCapability = (0, _pdfjsLib.createPromiseCapability)();
                }
              }, {
                key: "_shouldDirtyMatch",
                value: function _shouldDirtyMatch(cmd, state) {
                  if (state.query !== this._state.query) {
                    return true;
                  }

                  switch (cmd) {
                    case 'findagain':
                      const pageNumber = this._selected.pageIdx + 1;
                      const linkService = this._linkService;

                      if (pageNumber >= 1 && pageNumber <= linkService.pagesCount && pageNumber !== linkService.page && !linkService.isPageVisible(pageNumber)) {
                        return true;
                      }

                      return false;

                    case 'findhighlightallchange':
                      return false;
                  }

                  return true;
                }
              }, {
                key: "_prepareMatches",
                value: function _prepareMatches(matchesWithLength, matches, matchesLength) {
                  function isSubTerm(matchesWithLength, currentIndex) {
                    const currentElem = matchesWithLength[currentIndex];
                    const nextElem = matchesWithLength[currentIndex + 1];

                    if (currentIndex < matchesWithLength.length - 1 && currentElem.match === nextElem.match) {
                      currentElem.skipped = true;
                      return true;
                    }

                    for (let i = currentIndex - 1; i >= 0; i--) {
                      const prevElem = matchesWithLength[i];

                      if (prevElem.skipped) {
                        continue;
                      }

                      if (prevElem.match + prevElem.matchLength < currentElem.match) {
                        break;
                      }

                      if (prevElem.match + prevElem.matchLength >= currentElem.match + currentElem.matchLength) {
                        currentElem.skipped = true;
                        return true;
                      }
                    }

                    return false;
                  }

                  matchesWithLength.sort(function (a, b) {
                    return a.match === b.match ? a.matchLength - b.matchLength : a.match - b.match;
                  });

                  for (var i = 0, len = matchesWithLength.length; i < len; i++) {
                    if (isSubTerm(matchesWithLength, i)) {
                      continue;
                    }

                    matches.push(matchesWithLength[i].match);
                    matchesLength.push(matchesWithLength[i].matchLength);
                  }
                }
              }, {
                key: "_isEntireWord",
                value: function _isEntireWord(content, startIdx, length) {
                  if (startIdx > 0) {
                    const first = content.charCodeAt(startIdx);
                    const limit = content.charCodeAt(startIdx - 1);

                    if ((0, _pdf_find_utils.getCharacterType)(first) === (0, _pdf_find_utils.getCharacterType)(limit)) {
                      return false;
                    }
                  }

                  const endIdx = startIdx + length - 1;

                  if (endIdx < content.length - 1) {
                    const last = content.charCodeAt(endIdx);

                    const _limit = content.charCodeAt(endIdx + 1);

                    if ((0, _pdf_find_utils.getCharacterType)(last) === (0, _pdf_find_utils.getCharacterType)(_limit)) {
                      return false;
                    }
                  }

                  return true;
                }
              }, {
                key: "_calculatePhraseMatch",
                value: function _calculatePhraseMatch(query, pageIndex, pageContent, entireWord) {
                  const matches = [];
                  const queryLen = query.length;
                  let matchIdx = -queryLen;

                  while (true) {
                    matchIdx = pageContent.indexOf(query, matchIdx + queryLen);

                    if (matchIdx === -1) {
                      break;
                    }

                    if (entireWord && !this._isEntireWord(pageContent, matchIdx, queryLen)) {
                      continue;
                    }

                    matches.push(matchIdx);
                  }

                  this._pageMatches[pageIndex] = matches;
                }
              }, {
                key: "_calculateWordMatch",
                value: function _calculateWordMatch(query, pageIndex, pageContent, entireWord) {
                  const matchesWithLength = [];
                  const queryArray = query.match(/\S+/g);

                  let i = 0;
                  const len = queryArray.length;
                  for (; i < len; i++) {
                    const subquery = queryArray[i];
                    const subqueryLen = subquery.length;
                    let matchIdx = -subqueryLen;

                    while (true) {
                      matchIdx = pageContent.indexOf(subquery, matchIdx + subqueryLen);

                      if (matchIdx === -1) {
                        break;
                      }

                      if (entireWord && !this._isEntireWord(pageContent, matchIdx, subqueryLen)) {
                        continue;
                      }

                      matchesWithLength.push({
                        match: matchIdx,
                        matchLength: subqueryLen,
                        skipped: false
                      });
                    }
                  }

                  this._pageMatchesLength[pageIndex] = [];
                  this._pageMatches[pageIndex] = [];

                  this._prepareMatches(matchesWithLength, this._pageMatches[pageIndex], this._pageMatchesLength[pageIndex]);
                }
              }, {
                key: "_calculateMatch",
                value: function _calculateMatch(pageIndex) {
                  let pageContent = this._pageContents[pageIndex];
                  let query = this._query;
                  const _this$_state = this._state,
                      caseSensitive = _this$_state.caseSensitive,
                      entireWord = _this$_state.entireWord,
                      phraseSearch = _this$_state.phraseSearch;

                  if (query.length === 0) {
                    return;
                  }

                  if (!caseSensitive) {
                    pageContent = pageContent.toLowerCase();
                    query = query.toLowerCase();
                  }

                  if (phraseSearch) {
                    this._calculatePhraseMatch(query, pageIndex, pageContent, entireWord);
                  } else {
                    this._calculateWordMatch(query, pageIndex, pageContent, entireWord);
                  }

                  if (this._state.highlightAll) {
                    this._updatePage(pageIndex);
                  }

                  if (this._resumePageIdx === pageIndex) {
                    this._resumePageIdx = null;

                    this._nextPageMatch();
                  }

                  const pageMatchesCount = this._pageMatches[pageIndex].length;

                  if (pageMatchesCount > 0) {
                    this._matchesCountTotal += pageMatchesCount;

                    this._updateUIResultsCount();
                  }
                }
              }, {
                key: "_extractText",
                value: function _extractText() {
                  const _this2 = this;

                  if (this._extractTextPromises.length > 0) {
                    return;
                  }

                  let promise = Promise.resolve();

                  const _loop = function _loop(i, ii) {
                    const extractTextCapability = (0, _pdfjsLib.createPromiseCapability)();
                    _this2._extractTextPromises[i] = extractTextCapability.promise;
                    promise = promise.then(function () {
                      return _this2._pdfDocument.getPage(i + 1).then(function (pdfPage) {
                        return pdfPage.getTextContent({
                          normalizeWhitespace: true
                        });
                      }).then(function (textContent) {
                        const textItems = textContent.items;
                        const strBuf = [];

                        let j = 0;
                        const jj = textItems.length;
                        for (; j < jj; j++) {
                          strBuf.push(textItems[j].str);
                        }

                        _this2._pageContents[i] = normalize(strBuf.join(''));
                        extractTextCapability.resolve(i);
                      }, function (reason) {
                        console.error("Unable to get text content for page ".concat(i + 1), reason);
                        _this2._pageContents[i] = '';
                        extractTextCapability.resolve(i);
                      });
                    });
                  };

                  for (var i = 0, ii = this._linkService.pagesCount; i < ii; i++) {
                    _loop(i, ii);
                  }
                }
              }, {
                key: "_updatePage",
                value: function _updatePage(index) {
                  if (this._scrollMatches && this._selected.pageIdx === index) {
                    this._linkService.page = index + 1;
                  }

                  this._eventBus.dispatch('updatetextlayermatches', {
                    source: this,
                    pageIndex: index
                  });
                }
              }, {
                key: "_updateAllPages",
                value: function _updateAllPages() {
                  this._eventBus.dispatch('updatetextlayermatches', {
                    source: this,
                    pageIndex: -1
                  });
                }
              }, {
                key: "_nextMatch",
                value: function _nextMatch() {
                  const _this3 = this;

                  const previous = this._state.findPrevious;
                  const currentPageIndex = this._linkService.page - 1;
                  const numPages = this._linkService.pagesCount;
                  this._highlightMatches = true;

                  if (this._dirtyMatch) {
                    this._dirtyMatch = false;
                    this._selected.pageIdx = this._selected.matchIdx = -1;
                    this._offset.pageIdx = currentPageIndex;
                    this._offset.matchIdx = null;
                    this._offset.wrapped = false;
                    this._resumePageIdx = null;
                    this._pageMatches.length = 0;
                    this._pageMatchesLength.length = 0;
                    this._matchesCountTotal = 0;

                    this._updateAllPages();

                    for (let i = 0; i < numPages; i++) {
                      if (this._pendingFindMatches[i] === true) {
                        continue;
                      }

                      this._pendingFindMatches[i] = true;

                      this._extractTextPromises[i].then(function (pageIdx) {
                        delete _this3._pendingFindMatches[pageIdx];

                        _this3._calculateMatch(pageIdx);
                      });
                    }
                  }

                  if (this._query === '') {
                    this._updateUIState(FindState.FOUND);

                    return;
                  }

                  if (this._resumePageIdx) {
                    return;
                  }

                  const offset = this._offset;
                  this._pagesToSearch = numPages;

                  if (offset.matchIdx !== null) {
                    const numPageMatches = this._pageMatches[offset.pageIdx].length;

                    if (!previous && offset.matchIdx + 1 < numPageMatches || previous && offset.matchIdx > 0) {
                      offset.matchIdx = previous ? offset.matchIdx - 1 : offset.matchIdx + 1;

                      this._updateMatch(true);

                      return;
                    }

                    this._advanceOffsetPage(previous);
                  }

                  this._nextPageMatch();
                }
              }, {
                key: "_matchesReady",
                value: function _matchesReady(matches) {
                  const offset = this._offset;
                  const numMatches = matches.length;
                  const previous = this._state.findPrevious;

                  if (numMatches) {
                    offset.matchIdx = previous ? numMatches - 1 : 0;

                    this._updateMatch(true);

                    return true;
                  }

                  this._advanceOffsetPage(previous);

                  if (offset.wrapped) {
                    offset.matchIdx = null;

                    if (this._pagesToSearch < 0) {
                      this._updateMatch(false);

                      return true;
                    }
                  }

                  return false;
                }
              }, {
                key: "_nextPageMatch",
                value: function _nextPageMatch() {
                  if (this._resumePageIdx !== null) {
                    console.error('There can only be one pending page.');
                  }

                  let matches = null;

                  do {
                    const pageIdx = this._offset.pageIdx;
                    matches = this._pageMatches[pageIdx];

                    if (!matches) {
                      this._resumePageIdx = pageIdx;
                      break;
                    }
                  } while (!this._matchesReady(matches));
                }
              }, {
                key: "_advanceOffsetPage",
                value: function _advanceOffsetPage(previous) {
                  const offset = this._offset;
                  const numPages = this._linkService.pagesCount;
                  offset.pageIdx = previous ? offset.pageIdx - 1 : offset.pageIdx + 1;
                  offset.matchIdx = null;
                  this._pagesToSearch--;

                  if (offset.pageIdx >= numPages || offset.pageIdx < 0) {
                    offset.pageIdx = previous ? numPages - 1 : 0;
                    offset.wrapped = true;
                  }
                }
              }, {
                key: "_updateMatch",
                value: function _updateMatch() {
                  const found = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
                  let state = FindState.NOT_FOUND;
                  const wrapped = this._offset.wrapped;
                  this._offset.wrapped = false;

                  if (found) {
                    const previousPage = this._selected.pageIdx;
                    this._selected.pageIdx = this._offset.pageIdx;
                    this._selected.matchIdx = this._offset.matchIdx;
                    state = wrapped ? FindState.WRAPPED : FindState.FOUND;

                    if (previousPage !== -1 && previousPage !== this._selected.pageIdx) {
                      this._updatePage(previousPage);
                    }
                  }

                  this._updateUIState(state, this._state.findPrevious);

                  if (this._selected.pageIdx !== -1) {
                    this._scrollMatches = true;

                    this._updatePage(this._selected.pageIdx);
                  }
                }
              }, {
                key: "_onFindBarClose",
                value: function _onFindBarClose(evt) {
                  const _this4 = this;

                  const pdfDocument = this._pdfDocument;

                  this._firstPageCapability.promise.then(function () {
                    if (!_this4._pdfDocument || pdfDocument && _this4._pdfDocument !== pdfDocument) {
                      return;
                    }

                    if (_this4._findTimeout) {
                      clearTimeout(_this4._findTimeout);
                      _this4._findTimeout = null;
                    }

                    if (_this4._resumePageIdx) {
                      _this4._resumePageIdx = null;
                      _this4._dirtyMatch = true;
                    }

                    _this4._updateUIState(FindState.FOUND);

                    _this4._highlightMatches = false;

                    _this4._updateAllPages();
                  });
                }
              }, {
                key: "_requestMatchesCount",
                value: function _requestMatchesCount() {
                  const _this$_selected = this._selected,
                      pageIdx = _this$_selected.pageIdx,
                      matchIdx = _this$_selected.matchIdx;
                  let current = 0,
                      total = this._matchesCountTotal;

                  if (matchIdx !== -1) {
                    for (let i = 0; i < pageIdx; i++) {
                      current += this._pageMatches[i] && this._pageMatches[i].length || 0;
                    }

                    current += matchIdx + 1;
                  }

                  if (current < 1 || current > total) {
                    current = total = 0;
                  }

                  return {
                    current: current,
                    total: total
                  };
                }
              }, {
                key: "_updateUIResultsCount",
                value: function _updateUIResultsCount() {
                  this._eventBus.dispatch('updatefindmatchescount', {
                    source: this,
                    matchesCount: this._requestMatchesCount()
                  });
                }
              }, {
                key: "_updateUIState",
                value: function _updateUIState(state, previous) {
                  this._eventBus.dispatch('updatefindcontrolstate', {
                    source: this,
                    state: state,
                    previous: previous,
                    matchesCount: this._requestMatchesCount()
                  });
                }
              }, {
                key: "highlightMatches",
                get: function get() {
                  return this._highlightMatches;
                }
              }, {
                key: "pageMatches",
                get: function get() {
                  return this._pageMatches;
                }
              }, {
                key: "pageMatchesLength",
                get: function get() {
                  return this._pageMatchesLength;
                }
              }, {
                key: "selected",
                get: function get() {
                  return this._selected;
                }
              }, {
                key: "state",
                get: function get() {
                  return this._state;
                }
              }, {
                key: "_query",
                get: function get() {
                  if (this._state.query !== this._rawQuery) {
                    this._rawQuery = this._state.query;
                    this._normalizedQuery = normalize(this._state.query);
                  }

                  return this._normalizedQuery;
                }
              }]);

              return PDFFindController;
            }();

        exports.PDFFindController = PDFFindController;

/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getCharacterType = getCharacterType;
exports.CharacterType = void 0;
        const CharacterType = {
          SPACE: 0,
          ALPHA_LETTER: 1,
          PUNCT: 2,
          HAN_LETTER: 3,
          KATAKANA_LETTER: 4,
          HIRAGANA_LETTER: 5,
          HALFWIDTH_KATAKANA_LETTER: 6,
          THAI_LETTER: 7
        };
        exports.CharacterType = CharacterType;

function isAlphabeticalScript(charCode) {
  return charCode < 0x2E80;
}

function isAscii(charCode) {
  return (charCode & 0xFF80) === 0;
}

function isAsciiAlpha(charCode) {
  return charCode >= 0x61 && charCode <= 0x7A || charCode >= 0x41 && charCode <= 0x5A;
}

function isAsciiDigit(charCode) {
  return charCode >= 0x30 && charCode <= 0x39;
}

function isAsciiSpace(charCode) {
  return charCode === 0x20 || charCode === 0x09 || charCode === 0x0D || charCode === 0x0A;
}

function isHan(charCode) {
  return charCode >= 0x3400 && charCode <= 0x9FFF || charCode >= 0xF900 && charCode <= 0xFAFF;
}

function isKatakana(charCode) {
  return charCode >= 0x30A0 && charCode <= 0x30FF;
}

function isHiragana(charCode) {
  return charCode >= 0x3040 && charCode <= 0x309F;
}

function isHalfwidthKatakana(charCode) {
  return charCode >= 0xFF60 && charCode <= 0xFF9F;
}

function isThai(charCode) {
  return (charCode & 0xFF80) === 0x0E00;
}

function getCharacterType(charCode) {
  if (isAlphabeticalScript(charCode)) {
    if (isAscii(charCode)) {
      if (isAsciiSpace(charCode)) {
        return CharacterType.SPACE;
      } else if (isAsciiAlpha(charCode) || isAsciiDigit(charCode) || charCode === 0x5F) {
        return CharacterType.ALPHA_LETTER;
      }

      return CharacterType.PUNCT;
    } else if (isThai(charCode)) {
      return CharacterType.THAI_LETTER;
    } else if (charCode === 0xA0) {
      return CharacterType.SPACE;
    }

    return CharacterType.ALPHA_LETTER;
  }

  if (isHan(charCode)) {
    return CharacterType.HAN_LETTER;
  } else if (isKatakana(charCode)) {
    return CharacterType.KATAKANA_LETTER;
  } else if (isHiragana(charCode)) {
    return CharacterType.HIRAGANA_LETTER;
  } else if (isHalfwidthKatakana(charCode)) {
    return CharacterType.HALFWIDTH_KATAKANA_LETTER;
  }

  return CharacterType.ALPHA_LETTER;
}

/***/ }),
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isDestHashesEqual = isDestHashesEqual;
exports.isDestArraysEqual = isDestArraysEqual;
exports.PDFHistory = void 0;

        const _ui_utils = __webpack_require__(5);

        function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) { return; }
  const _arr = [];
  let _n = true;
  let _d = false;
  let _e = undefined;
  try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const HASH_CHANGE_TIMEOUT = 1000;
        const POSITION_UPDATED_THRESHOLD = 50;
        const UPDATE_VIEWAREA_TIMEOUT = 1000;

        function getCurrentHash() {
  return document.location.hash;
}

function parseCurrentHash(linkService) {
  const hash = unescape(getCurrentHash()).substring(1);
  const params = (0, _ui_utils.parseQueryString)(hash);
  let page = params.page | 0;

  if (!(Number.isInteger(page) && page > 0 && page <= linkService.pagesCount)) {
    page = null;
  }

  return {
    hash: hash,
    page: page,
    rotation: linkService.rotation
  };
}

        const PDFHistory =
            /*#__PURE__*/
            function () {
              function PDFHistory(_ref) {
                const _this = this;

                const linkService = _ref.linkService,
                    eventBus = _ref.eventBus;

                _classCallCheck(this, PDFHistory);

                this.linkService = linkService;
                this.eventBus = eventBus || (0, _ui_utils.getGlobalEventBus)();
                this.initialized = false;
                this.initialBookmark = null;
                this.initialRotation = null;
                this._boundEvents = Object.create(null);
                this._isViewerInPresentationMode = false;
                this._isPagesLoaded = false;
                this.eventBus.on('presentationmodechanged', function (evt) {
                  _this._isViewerInPresentationMode = evt.active || evt.switchInProgress;
                });
                this.eventBus.on('pagesloaded', function (evt) {
                  _this._isPagesLoaded = !!evt.pagesCount;
                });
              }

              _createClass(PDFHistory, [{
                key: "initialize",
                value: function initialize(_ref2) {
                  const fingerprint = _ref2.fingerprint,
                      _ref2$resetHistory = _ref2.resetHistory,
                      resetHistory = _ref2$resetHistory === void 0 ? false : _ref2$resetHistory,
                      _ref2$updateUrl = _ref2.updateUrl,
                      updateUrl = _ref2$updateUrl === void 0 ? false : _ref2$updateUrl;

                  if (!fingerprint || typeof fingerprint !== 'string') {
                    console.error('PDFHistory.initialize: The "fingerprint" must be a non-empty string.');
                    return;
                  }

                  const reInitialized = this.initialized && this.fingerprint !== fingerprint;
                  this.fingerprint = fingerprint;
                  this._updateUrl = updateUrl === true;

                  if (!this.initialized) {
                    this._bindEvents();
                  }

                  const state = window.history.state;
                  this.initialized = true;
                  this.initialBookmark = null;
                  this.initialRotation = null;
                  this._popStateInProgress = false;
                  this._blockHashChange = 0;
                  this._currentHash = getCurrentHash();
                  this._numPositionUpdates = 0;
                  this._uid = this._maxUid = 0;
                  this._destination = null;
                  this._position = null;

                  if (!this._isValidState(state, true) || resetHistory) {
                    const _parseCurrentHash = parseCurrentHash(this.linkService),
                        hash = _parseCurrentHash.hash,
                        page = _parseCurrentHash.page,
                        rotation = _parseCurrentHash.rotation;

                    if (!hash || reInitialized || resetHistory) {
                      this._pushOrReplaceState(null, true);

                      return;
                    }

                    this._pushOrReplaceState({
                      hash: hash,
                      page: page,
                      rotation: rotation
                    }, true);

                    return;
                  }

                  const destination = state.destination;

                  this._updateInternalState(destination, state.uid, true);

                  if (this._uid > this._maxUid) {
                    this._maxUid = this._uid;
                  }

                  if (destination.rotation !== undefined) {
                    this.initialRotation = destination.rotation;
                  }

                  if (destination.dest) {
                    this.initialBookmark = JSON.stringify(destination.dest);
                    this._destination.page = null;
                  } else if (destination.hash) {
                    this.initialBookmark = destination.hash;
                  } else if (destination.page) {
                    this.initialBookmark = "page=".concat(destination.page);
                  }
                }
              }, {
                key: "push",
                value: function push(_ref3) {
                  const _this2 = this;

                  const _ref3$namedDest = _ref3.namedDest,
                      namedDest = _ref3$namedDest === void 0 ? null : _ref3$namedDest,
                      explicitDest = _ref3.explicitDest,
                      pageNumber = _ref3.pageNumber;

                  if (!this.initialized) {
                    return;
                  }

                  if (namedDest && typeof namedDest !== 'string') {
                    console.error('PDFHistory.push: ' + "\"".concat(namedDest, "\" is not a valid namedDest parameter."));
                    return;
                  } else if (!Array.isArray(explicitDest)) {
                    console.error('PDFHistory.push: ' + "\"".concat(explicitDest, "\" is not a valid explicitDest parameter."));
                    return;
                  } else if (!(Number.isInteger(pageNumber) && pageNumber > 0 && pageNumber <= this.linkService.pagesCount)) {
                    if (pageNumber !== null || this._destination) {
                      console.error('PDFHistory.push: ' + "\"".concat(pageNumber, "\" is not a valid pageNumber parameter."));
                      return;
                    }
                  }

                  const hash = namedDest || JSON.stringify(explicitDest);

                  if (!hash) {
                    return;
                  }

                  let forceReplace = false;

                  if (this._destination && (isDestHashesEqual(this._destination.hash, hash) || isDestArraysEqual(this._destination.dest, explicitDest))) {
                    if (this._destination.page) {
                      return;
                    }

                    forceReplace = true;
                  }

                  if (this._popStateInProgress && !forceReplace) {
                    return;
                  }

                  this._pushOrReplaceState({
                    dest: explicitDest,
                    hash: hash,
                    page: pageNumber,
                    rotation: this.linkService.rotation
                  }, forceReplace);

                  if (!this._popStateInProgress) {
                    this._popStateInProgress = true;
                    Promise.resolve().then(function () {
                      _this2._popStateInProgress = false;
                    });
                  }
                }
              }, {
                key: "pushCurrentPosition",
                value: function pushCurrentPosition() {
                  if (!this.initialized || this._popStateInProgress) {
                    return;
                  }

                  this._tryPushCurrentPosition();
                }
              }, {
                key: "back",
                value: function back() {
                  if (!this.initialized || this._popStateInProgress) {
                    return;
                  }

                  const state = window.history.state;

                  if (this._isValidState(state) && state.uid > 0) {
                    window.history.back();
                  }
                }
              }, {
                key: "forward",
                value: function forward() {
                  if (!this.initialized || this._popStateInProgress) {
                    return;
                  }

                  const state = window.history.state;

                  if (this._isValidState(state) && state.uid < this._maxUid) {
                    window.history.forward();
                  }
                }
              }, {
                key: "_pushOrReplaceState",
                value: function _pushOrReplaceState(destination) {
                  const forceReplace = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  const shouldReplace = forceReplace || !this._destination;
                  const newState = {
                    fingerprint: this.fingerprint,
                    uid: shouldReplace ? this._uid : this._uid + 1,
                    destination: destination
                  };

                  this._updateInternalState(destination, newState.uid);

                  let newUrl;

                  if (this._updateUrl && destination && destination.hash) {
                    const baseUrl = document.location.href.split('#')[0];

                    if (!baseUrl.startsWith('file://')) {
                      newUrl = "".concat(baseUrl, "#").concat(destination.hash);
                    }
                  }

                  if (shouldReplace) {
                    if (newUrl) {
                      window.history.replaceState(newState, '', newUrl);
                    } else {
                      window.history.replaceState(newState, '');
                    }
                  } else {
                    this._maxUid = this._uid;

                    if (newUrl) {
                      window.history.pushState(newState, '', newUrl);
                    } else {
                      window.history.pushState(newState, '');
                    }
                  }
                }
              }, {
                key: "_tryPushCurrentPosition",
                value: function _tryPushCurrentPosition() {
                  const temporary = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

                  if (!this._position) {
                    return;
                  }

                  let position = this._position;

                  if (temporary) {
                    position = Object.assign(Object.create(null), this._position);
                    position.temporary = true;
                  }

                  if (!this._destination) {
                    this._pushOrReplaceState(position);

                    return;
                  }

                  if (this._destination.temporary) {
                    this._pushOrReplaceState(position, true);

                    return;
                  }

                  if (this._destination.hash === position.hash) {
                    return;
                  }

                  if (!this._destination.page && (POSITION_UPDATED_THRESHOLD <= 0 || this._numPositionUpdates <= POSITION_UPDATED_THRESHOLD)) {
                    return;
                  }

                  let forceReplace = false;

                  if (this._destination.page >= position.first && this._destination.page <= position.page) {
                    if (this._destination.dest || !this._destination.first) {
                      return;
                    }

                    forceReplace = true;
                  }

                  this._pushOrReplaceState(position, forceReplace);
                }
              }, {
                key: "_isValidState",
                value: function _isValidState(state) {
                  const checkReload = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

                  if (!state) {
                    return false;
                  }

                  if (state.fingerprint !== this.fingerprint) {
                    if (checkReload) {
                      if (typeof state.fingerprint !== 'string' || state.fingerprint.length !== this.fingerprint.length) {
                        return false;
                      }

                      const _performance$getEntri = performance.getEntriesByType('navigation'),
                          _performance$getEntri2 = _slicedToArray(_performance$getEntri, 1),
                          perfEntry = _performance$getEntri2[0];

                      if (!perfEntry || perfEntry.type !== 'reload') {
                        return false;
                      }
                    } else {
                      return false;
                    }
                  }

                  if (!Number.isInteger(state.uid) || state.uid < 0) {
                    return false;
                  }

                  if (state.destination === null || _typeof(state.destination) !== 'object') {
                    return false;
                  }

                  return true;
                }
              }, {
                key: "_updateInternalState",
                value: function _updateInternalState(destination, uid) {
                  const removeTemporary = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

                  if (this._updateViewareaTimeout) {
                    clearTimeout(this._updateViewareaTimeout);
                    this._updateViewareaTimeout = null;
                  }

                  if (removeTemporary && destination && destination.temporary) {
                    delete destination.temporary;
                  }

                  this._destination = destination;
                  this._uid = uid;
                  this._numPositionUpdates = 0;
                }
              }, {
                key: "_updateViewarea",
                value: function _updateViewarea(_ref4) {
                  const _this3 = this;

                  const location = _ref4.location;

                  if (this._updateViewareaTimeout) {
                    clearTimeout(this._updateViewareaTimeout);
                    this._updateViewareaTimeout = null;
                  }

                  this._position = {
                    hash: this._isViewerInPresentationMode ? "page=".concat(location.pageNumber) : location.pdfOpenParams.substring(1),
                    page: this.linkService.page,
                    first: location.pageNumber,
                    rotation: location.rotation
                  };

                  if (this._popStateInProgress) {
                    return;
                  }

                  if (POSITION_UPDATED_THRESHOLD > 0 && this._isPagesLoaded && this._destination && !this._destination.page) {
                    this._numPositionUpdates++;
                  }

                  if (UPDATE_VIEWAREA_TIMEOUT > 0) {
                    this._updateViewareaTimeout = setTimeout(function () {
                      if (!_this3._popStateInProgress) {
                        _this3._tryPushCurrentPosition(true);
                      }

                      _this3._updateViewareaTimeout = null;
                    }, UPDATE_VIEWAREA_TIMEOUT);
                  }
                }
              }, {
                key: "_popState",
                value: function _popState(_ref5) {
                  const _this4 = this;

                  const state = _ref5.state;
                  const newHash = getCurrentHash(),
                      hashChanged = this._currentHash !== newHash;
                  this._currentHash = newHash;

                  if (!state || false) {
                    this._uid++;

                    const _parseCurrentHash2 = parseCurrentHash(this.linkService),
                        hash = _parseCurrentHash2.hash,
                        page = _parseCurrentHash2.page,
                        rotation = _parseCurrentHash2.rotation;

                    this._pushOrReplaceState({
                      hash: hash,
                      page: page,
                      rotation: rotation
                    }, true);

                    return;
                  }

                  if (!this._isValidState(state)) {
                    return;
                  }

                  this._popStateInProgress = true;

                  if (hashChanged) {
                    this._blockHashChange++;
                    (0, _ui_utils.waitOnEventOrTimeout)({
                      target: window,
                      name: 'hashchange',
                      delay: HASH_CHANGE_TIMEOUT
                    }).then(function () {
                      _this4._blockHashChange--;
                    });
                  }

                  const destination = state.destination;

                  this._updateInternalState(destination, state.uid, true);

                  if (this._uid > this._maxUid) {
                    this._maxUid = this._uid;
                  }

                  if ((0, _ui_utils.isValidRotation)(destination.rotation)) {
                    this.linkService.rotation = destination.rotation;
                  }

                  if (destination.dest) {
                    this.linkService.navigateTo(destination.dest);
                  } else if (destination.hash) {
                    this.linkService.setHash(destination.hash);
                  } else if (destination.page) {
                    this.linkService.page = destination.page;
                  }

                  Promise.resolve().then(function () {
                    _this4._popStateInProgress = false;
                  });
                }
              }, {
                key: "_bindEvents",
                value: function _bindEvents() {
                  const _this5 = this;

                  const _boundEvents = this._boundEvents,
                      eventBus = this.eventBus;
                  _boundEvents.updateViewarea = this._updateViewarea.bind(this);
                  _boundEvents.popState = this._popState.bind(this);

                  _boundEvents.pageHide = function (evt) {
                    if (!_this5._destination || _this5._destination.temporary) {
                      _this5._tryPushCurrentPosition();
                    }
                  };

                  eventBus.on('updateviewarea', _boundEvents.updateViewarea);
                  window.addEventListener('popstate', _boundEvents.popState);
                  window.addEventListener('pagehide', _boundEvents.pageHide);
                }
              }, {
                key: "popStateInProgress",
                get: function get() {
                  return this.initialized && (this._popStateInProgress || this._blockHashChange > 0);
                }
              }]);

              return PDFHistory;
            }();

        exports.PDFHistory = PDFHistory;

function isDestHashesEqual(destHash, pushHash) {
  if (typeof destHash !== 'string' || typeof pushHash !== 'string') {
    return false;
  }

  if (destHash === pushHash) {
    return true;
  }

  const _parseQueryString = (0, _ui_utils.parseQueryString)(destHash),
      nameddest = _parseQueryString.nameddest;

  if (nameddest === pushHash) {
    return true;
  }

  return false;
}

function isDestArraysEqual(firstDest, secondDest) {
  function isEntryEqual(first, second) {
    if (_typeof(first) !== _typeof(second)) {
      return false;
    }

    if (Array.isArray(first) || Array.isArray(second)) {
      return false;
    }

    if (first !== null && _typeof(first) === 'object' && second !== null) {
      if (Object.keys(first).length !== Object.keys(second).length) {
        return false;
      }

      for (let key in first) {
        if (!isEntryEqual(first[key], second[key])) {
          return false;
        }
      }

      return true;
    }

    return first === second || Number.isNaN(first) && Number.isNaN(second);
  }

  if (!(Array.isArray(firstDest) && Array.isArray(secondDest))) {
    return false;
  }

  if (firstDest.length !== secondDest.length) {
    return false;
  }

  let i = 0;
  const ii = firstDest.length;
  for (; i < ii; i++) {
    if (!isEntryEqual(firstDest[i], secondDest[i])) {
      return false;
    }
  }

  return true;
}

/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SimpleLinkService = exports.PDFLinkService = void 0;

        const _ui_utils = __webpack_require__(5);

        function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const PDFLinkService =
            /*#__PURE__*/
            function () {
              function PDFLinkService() {
                const _ref = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
                    eventBus = _ref.eventBus,
                    _ref$externalLinkTarg = _ref.externalLinkTarget,
                    externalLinkTarget = _ref$externalLinkTarg === void 0 ? null : _ref$externalLinkTarg,
                    _ref$externalLinkRel = _ref.externalLinkRel,
                    externalLinkRel = _ref$externalLinkRel === void 0 ? null : _ref$externalLinkRel,
                    _ref$externalLinkEnab = _ref.externalLinkEnabled,
                    externalLinkEnabled = _ref$externalLinkEnab === void 0 ? true : _ref$externalLinkEnab;

                _classCallCheck(this, PDFLinkService);

                this.eventBus = eventBus || (0, _ui_utils.getGlobalEventBus)();
                this.externalLinkTarget = externalLinkTarget;
                this.externalLinkRel = externalLinkRel;
                this.externalLinkEnabled = externalLinkEnabled;
                this.baseUrl = null;
                this.pdfDocument = null;
                this.pdfViewer = null;
                this.pdfHistory = null;
                this._pagesRefCache = null;
              }

              _createClass(PDFLinkService, [{
                key: "setDocument",
                value: function setDocument(pdfDocument) {
                  const baseUrl = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
                  this.baseUrl = baseUrl;
                  this.pdfDocument = pdfDocument;
                  this._pagesRefCache = Object.create(null);
                }
              }, {
                key: "setViewer",
                value: function setViewer(pdfViewer) {
                  this.pdfViewer = pdfViewer;
                }
              }, {
                key: "setHistory",
                value: function setHistory(pdfHistory) {
                  this.pdfHistory = pdfHistory;
                }
              }, {
                key: "navigateTo",
                value: function navigateTo(dest) {
                  const _this = this;

                  const goToDestination = function goToDestination(_ref2) {
                    const namedDest = _ref2.namedDest,
                        explicitDest = _ref2.explicitDest;
                    const destRef = explicitDest[0];
                    let pageNumber;

                    if (destRef instanceof Object) {
                      pageNumber = _this._cachedPageNumber(destRef);

                      if (pageNumber === null) {
                        _this.pdfDocument.getPageIndex(destRef).then(function (pageIndex) {
                          _this.cachePageRef(pageIndex + 1, destRef);

                          goToDestination({
                            namedDest: namedDest,
                            explicitDest: explicitDest
                          });
                        })["catch"](function () {
                          console.error("PDFLinkService.navigateTo: \"".concat(destRef, "\" is not ") + "a valid page reference, for dest=\"".concat(dest, "\"."));
                        });

                        return;
                      }
                    } else if (Number.isInteger(destRef)) {
                      pageNumber = destRef + 1;
                    } else {
                      console.error("PDFLinkService.navigateTo: \"".concat(destRef, "\" is not ") + "a valid destination reference, for dest=\"".concat(dest, "\"."));
                      return;
                    }

                    if (!pageNumber || pageNumber < 1 || pageNumber > _this.pagesCount) {
                      console.error("PDFLinkService.navigateTo: \"".concat(pageNumber, "\" is not ") + "a valid page number, for dest=\"".concat(dest, "\"."));
                      return;
                    }

                    if (_this.pdfHistory) {
                      _this.pdfHistory.pushCurrentPosition();

                      _this.pdfHistory.push({
                        namedDest: namedDest,
                        explicitDest: explicitDest,
                        pageNumber: pageNumber
                      });
                    }

                    _this.pdfViewer.scrollPageIntoView({
                      pageNumber: pageNumber,
                      destArray: explicitDest
                    });
                  };

                  new Promise(function (resolve, reject) {
                    if (typeof dest === 'string') {
                      _this.pdfDocument.getDestination(dest).then(function (destArray) {
                        resolve({
                          namedDest: dest,
                          explicitDest: destArray
                        });
                      });

                      return;
                    }

                    resolve({
                      namedDest: '',
                      explicitDest: dest
                    });
                  }).then(function (data) {
                    if (!Array.isArray(data.explicitDest)) {
                      console.error("PDFLinkService.navigateTo: \"".concat(data.explicitDest, "\" is") + " not a valid destination array, for dest=\"".concat(dest, "\"."));
                      return;
                    }

                    goToDestination(data);
                  });
                }
              }, {
                key: "getDestinationHash",
                value: function getDestinationHash(dest) {
                  if (typeof dest === 'string') {
                    return this.getAnchorUrl('#' + escape(dest));
                  }

                  if (Array.isArray(dest)) {
                    const str = JSON.stringify(dest);
                    return this.getAnchorUrl('#' + escape(str));
                  }

                  return this.getAnchorUrl('');
                }
              }, {
                key: "getAnchorUrl",
                value: function getAnchorUrl(anchor) {
                  return (this.baseUrl || '') + anchor;
                }
              }, {
                key: "setHash",
                value: function setHash(hash) {
                  let pageNumber, dest;

                  if (hash.includes('=')) {
                    const params = (0, _ui_utils.parseQueryString)(hash);

                    if ('search' in params) {
                      this.eventBus.dispatch('findfromurlhash', {
                        source: this,
                        query: params['search'].replace(/"/g, ''),
                        phraseSearch: params['phrase'] === 'true'
                      });
                    }

                    if ('nameddest' in params) {
                      this.navigateTo(params.nameddest);
                      return;
                    }

                    if ('page' in params) {
                      pageNumber = params.page | 0 || 1;
                    }

                    if ('zoom' in params) {
                      const zoomArgs = params.zoom.split(',');
                      const zoomArg = zoomArgs[0];
                      const zoomArgNumber = parseFloat(zoomArg);

                      if (!zoomArg.includes('Fit')) {
                        dest = [null, {
                          name: 'XYZ'
                        }, zoomArgs.length > 1 ? zoomArgs[1] | 0 : null, zoomArgs.length > 2 ? zoomArgs[2] | 0 : null, zoomArgNumber ? zoomArgNumber / 100 : zoomArg];
                      } else {
                        if (zoomArg === 'Fit' || zoomArg === 'FitB') {
                          dest = [null, {
                            name: zoomArg
                          }];
                        } else if (zoomArg === 'FitH' || zoomArg === 'FitBH' || zoomArg === 'FitV' || zoomArg === 'FitBV') {
                          dest = [null, {
                            name: zoomArg
                          }, zoomArgs.length > 1 ? zoomArgs[1] | 0 : null];
                        } else if (zoomArg === 'FitR') {
                          if (zoomArgs.length !== 5) {
                            console.error('PDFLinkService.setHash: Not enough parameters for "FitR".');
                          } else {
                            dest = [null, {
                              name: zoomArg
                            }, zoomArgs[1] | 0, zoomArgs[2] | 0, zoomArgs[3] | 0, zoomArgs[4] | 0];
                          }
                        } else {
                          console.error("PDFLinkService.setHash: \"".concat(zoomArg, "\" is not ") + 'a valid zoom value.');
                        }
                      }
                    }

                    if (dest) {
                      this.pdfViewer.scrollPageIntoView({
                        pageNumber: pageNumber || this.page,
                        destArray: dest,
                        allowNegativeOffset: true
                      });
                    } else if (pageNumber) {
                      this.page = pageNumber;
                    }

                    if ('pagemode' in params) {
                      this.eventBus.dispatch('pagemode', {
                        source: this,
                        mode: params.pagemode
                      });
                    }
                  } else {
                    dest = unescape(hash);

                    try {
                      dest = JSON.parse(dest);

                      if (!Array.isArray(dest)) {
                        dest = dest.toString();
                      }
                    } catch (ex) {
                    }

                    if (typeof dest === 'string' || isValidExplicitDestination(dest)) {
                      this.navigateTo(dest);
                      return;
                    }

                    console.error("PDFLinkService.setHash: \"".concat(unescape(hash), "\" is not ") + 'a valid destination.');
                  }
                }
              }, {
                key: "executeNamedAction",
                value: function executeNamedAction(action) {
                  switch (action) {
                    case 'GoBack':
                      if (this.pdfHistory) {
                        this.pdfHistory.back();
                      }

                      break;

                    case 'GoForward':
                      if (this.pdfHistory) {
                        this.pdfHistory.forward();
                      }

                      break;

                    case 'NextPage':
                      if (this.page < this.pagesCount) {
                        this.page++;
                      }

                      break;

                    case 'PrevPage':
                      if (this.page > 1) {
                        this.page--;
                      }

                      break;

                    case 'LastPage':
                      this.page = this.pagesCount;
                      break;

                    case 'FirstPage':
                      this.page = 1;
                      break;

                    default:
                      break;
                  }

                  this.eventBus.dispatch('namedaction', {
                    source: this,
                    action: action
                  });
                }
              }, {
                key: "cachePageRef",
                value: function cachePageRef(pageNum, pageRef) {
                  if (!pageRef) {
                    return;
                  }

                  const refStr = pageRef.gen === 0 ? "".concat(pageRef.num, "R") : "".concat(pageRef.num, "R").concat(pageRef.gen);
                  this._pagesRefCache[refStr] = pageNum;
                }
              }, {
                key: "_cachedPageNumber",
                value: function _cachedPageNumber(pageRef) {
                  const refStr = pageRef.gen === 0 ? "".concat(pageRef.num, "R") : "".concat(pageRef.num, "R").concat(pageRef.gen);
                  return this._pagesRefCache && this._pagesRefCache[refStr] || null;
                }
              }, {
                key: "isPageVisible",
                value: function isPageVisible(pageNumber) {
                  return this.pdfViewer.isPageVisible(pageNumber);
                }
              }, {
                key: "pagesCount",
                get: function get() {
                  return this.pdfDocument ? this.pdfDocument.numPages : 0;
                }
              }, {
                key: "page",
                get: function get() {
                  return this.pdfViewer.currentPageNumber;
                },
                set: function set(value) {
                  this.pdfViewer.currentPageNumber = value;
                }
              }, {
                key: "rotation",
                get: function get() {
                  return this.pdfViewer.pagesRotation;
                },
                set: function set(value) {
                  this.pdfViewer.pagesRotation = value;
                }
              }]);

              return PDFLinkService;
            }();

        exports.PDFLinkService = PDFLinkService;

function isValidExplicitDestination(dest) {
  if (!Array.isArray(dest)) {
    return false;
  }

  const destLength = dest.length;
  let allowNull = true;

  if (destLength < 2) {
    return false;
  }

  const page = dest[0];

  if (!(_typeof(page) === 'object' && Number.isInteger(page.num) && Number.isInteger(page.gen)) && !(Number.isInteger(page) && page >= 0)) {
    return false;
  }

  const zoom = dest[1];

  if (!(_typeof(zoom) === 'object' && typeof zoom.name === 'string')) {
    return false;
  }

  switch (zoom.name) {
    case 'XYZ':
      if (destLength !== 5) {
        return false;
      }

      break;

    case 'Fit':
    case 'FitB':
      return destLength === 2;

    case 'FitH':
    case 'FitBH':
    case 'FitV':
    case 'FitBV':
      if (destLength !== 3) {
        return false;
      }

      break;

    case 'FitR':
      if (destLength !== 6) {
        return false;
      }

      allowNull = false;
      break;

    default:
      return false;
  }

  for (let i = 2; i < destLength; i++) {
    const param = dest[i];

    if (!(typeof param === 'number' || allowNull && param === null)) {
      return false;
    }
  }

  return true;
}

        const SimpleLinkService =
            /*#__PURE__*/
            function () {
              function SimpleLinkService() {
                _classCallCheck(this, SimpleLinkService);

                this.externalLinkTarget = null;
                this.externalLinkRel = null;
                this.externalLinkEnabled = true;
              }

              _createClass(SimpleLinkService, [{
                key: "navigateTo",
                value: function navigateTo(dest) {
                }
              }, {
                key: "getDestinationHash",
                value: function getDestinationHash(dest) {
                  return '#';
                }
              }, {
                key: "getAnchorUrl",
                value: function getAnchorUrl(hash) {
                  return '#';
                }
              }, {
                key: "setHash",
                value: function setHash(hash) {
                }
              }, {
                key: "executeNamedAction",
                value: function executeNamedAction(action) {
                }
              }, {
                key: "cachePageRef",
                value: function cachePageRef(pageNum, pageRef) {
                }
              }, {
                key: "isPageVisible",
                value: function isPageVisible(pageNumber) {
                  return true;
                }
              }, {
                key: "pagesCount",
                get: function get() {
                  return 0;
                }
              }, {
                key: "page",
                get: function get() {
                  return 0;
                },
                set: function set(value) {
                }
              }, {
                key: "rotation",
                get: function get() {
                  return 0;
                },
                set: function set(value) {
                }
              }]);

              return SimpleLinkService;
            }();

        exports.SimpleLinkService = SimpleLinkService;

/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFOutlineViewer = void 0;

        const _pdfjsLib = __webpack_require__(7);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const DEFAULT_TITLE = "\u2013";

        const PDFOutlineViewer =
            /*#__PURE__*/
            function () {
              function PDFOutlineViewer(_ref) {
                const container = _ref.container,
                    linkService = _ref.linkService,
                    eventBus = _ref.eventBus;

                _classCallCheck(this, PDFOutlineViewer);

                this.container = container;
                this.linkService = linkService;
                this.eventBus = eventBus;
                this.reset();
                eventBus.on('toggleoutlinetree', this.toggleOutlineTree.bind(this));
              }

              _createClass(PDFOutlineViewer, [{
                key: "reset",
                value: function reset() {
                  this.outline = null;
                  this.lastToggleIsShow = true;
                  this.container.textContent = '';
                  this.container.classList.remove('outlineWithDeepNesting');
                }
              }, {
                key: "_dispatchEvent",
                value: function _dispatchEvent(outlineCount) {
                  this.eventBus.dispatch('outlineloaded', {
                    source: this,
                    outlineCount: outlineCount
                  });
                }
              }, {
                key: "_bindLink",
                value: function _bindLink(element, _ref2) {
                  const url = _ref2.url,
                      newWindow = _ref2.newWindow,
                      dest = _ref2.dest;
                  const linkService = this.linkService;

                  if (url) {
                    (0, _pdfjsLib.addLinkAttributes)(element, {
                      url: url,
                      target: newWindow ? _pdfjsLib.LinkTarget.BLANK : linkService.externalLinkTarget,
                      rel: linkService.externalLinkRel,
                      enabled: linkService.externalLinkEnabled
                    });
                    return;
                  }

                  element.href = linkService.getDestinationHash(dest);

                  element.onclick = function () {
                    if (dest) {
                      linkService.navigateTo(dest);
                    }

                    return false;
                  };
                }
              }, {
                key: "_setStyles",
                value: function _setStyles(element, _ref3) {
                  const bold = _ref3.bold,
                      italic = _ref3.italic;
                  let styleStr = '';

                  if (bold) {
                    styleStr += 'font-weight: bold;';
                  }

                  if (italic) {
                    styleStr += 'font-style: italic;';
                  }

                  if (styleStr) {
                    element.setAttribute('style', styleStr);
                  }
                }
              }, {
                key: "_addToggleButton",
                value: function _addToggleButton(div, _ref4) {
                  const _this = this;

                  const count = _ref4.count,
                      items = _ref4.items;
                  const toggler = document.createElement('div');
                  toggler.className = 'outlineItemToggler';

                  if (count < 0 && Math.abs(count) === items.length) {
                    toggler.classList.add('outlineItemsHidden');
                  }

                  toggler.onclick = function (evt) {
                    evt.stopPropagation();
                    toggler.classList.toggle('outlineItemsHidden');

                    if (evt.shiftKey) {
                      const shouldShowAll = !toggler.classList.contains('outlineItemsHidden');

                      _this._toggleOutlineItem(div, shouldShowAll);
                    }
                  };

                  div.insertBefore(toggler, div.firstChild);
                }
              }, {
                key: "_toggleOutlineItem",
                value: function _toggleOutlineItem(root) {
                  const show = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  this.lastToggleIsShow = show;
                  let _iteratorNormalCompletion = true;
                  let _didIteratorError = false;
                  let _iteratorError = undefined;

                  try {
                    for (var _iterator = root.querySelectorAll('.outlineItemToggler')[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                      const toggler = _step.value;
                      toggler.classList.toggle('outlineItemsHidden', !show);
                    }
                  } catch (err) {
                    _didIteratorError = true;
                    _iteratorError = err;
                  } finally {
                    try {
                      if (!_iteratorNormalCompletion && _iterator["return"] != null) {
                        _iterator["return"]();
                      }
                    } finally {
                      if (_didIteratorError) {
                        throw _iteratorError;
                      }
                    }
                  }
                }
              }, {
                key: "toggleOutlineTree",
                value: function toggleOutlineTree() {
                  if (!this.outline) {
                    return;
                  }

                  this._toggleOutlineItem(this.container, !this.lastToggleIsShow);
                }
              }, {
                key: "render",
                value: function render(_ref5) {
                  const outline = _ref5.outline;
                  let outlineCount = 0;

                  if (this.outline) {
                    this.reset();
                  }

                  this.outline = outline || null;

                  if (!outline) {
                    this._dispatchEvent(outlineCount);

                    return;
                  }

                  const fragment = document.createDocumentFragment();
                  const queue = [{
                    parent: fragment,
                    items: this.outline
                  }];
                  let hasAnyNesting = false;

                  while (queue.length > 0) {
                    const levelData = queue.shift();
                    let _iteratorNormalCompletion2 = true;
                    let _didIteratorError2 = false;
                    let _iteratorError2 = undefined;

                    try {
                      for (var _iterator2 = levelData.items[Symbol.iterator](), _step2; !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
                        const item = _step2.value;
                        const div = document.createElement('div');
                        div.className = 'outlineItem';
                        const element = document.createElement('a');

                        this._bindLink(element, item);

                        this._setStyles(element, item);

                        element.textContent = (0, _pdfjsLib.removeNullCharacters)(item.title) || DEFAULT_TITLE;
                        div.appendChild(element);

                        if (item.items.length > 0) {
                          hasAnyNesting = true;

                          this._addToggleButton(div, item);

                          const itemsDiv = document.createElement('div');
                          itemsDiv.className = 'outlineItems';
                          div.appendChild(itemsDiv);
                          queue.push({
                            parent: itemsDiv,
                            items: item.items
                          });
                        }

                        levelData.parent.appendChild(div);
                        outlineCount++;
                      }
                    } catch (err) {
                      _didIteratorError2 = true;
                      _iteratorError2 = err;
                    } finally {
                      try {
                        if (!_iteratorNormalCompletion2 && _iterator2["return"] != null) {
                          _iterator2["return"]();
                        }
                      } finally {
                        if (_didIteratorError2) {
                          throw _iteratorError2;
                        }
                      }
                    }
                  }

                  if (hasAnyNesting) {
                    this.container.classList.add('outlineWithDeepNesting');
                    this.lastToggleIsShow = fragment.querySelectorAll('.outlineItemsHidden').length === 0;
                  }

                  this.container.appendChild(fragment);

                  this._dispatchEvent(outlineCount);
                }
              }]);

              return PDFOutlineViewer;
            }();

        exports.PDFOutlineViewer = PDFOutlineViewer;

/***/ }),
/* 23 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFPresentationMode = void 0;

        const _ui_utils = __webpack_require__(5);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const DELAY_BEFORE_RESETTING_SWITCH_IN_PROGRESS = 1500;
        const DELAY_BEFORE_HIDING_CONTROLS = 3000;
        const ACTIVE_SELECTOR = 'pdfPresentationMode';
        const CONTROLS_SELECTOR = 'pdfPresentationModeControls';
        const MOUSE_SCROLL_COOLDOWN_TIME = 50;
        const PAGE_SWITCH_THRESHOLD = 0.1;
        const SWIPE_MIN_DISTANCE_THRESHOLD = 50;
        const SWIPE_ANGLE_THRESHOLD = Math.PI / 6;

        const PDFPresentationMode =
            /*#__PURE__*/
            function () {
              function PDFPresentationMode(_ref) {
                const _this = this;

                const container = _ref.container,
                    _ref$viewer = _ref.viewer,
                    viewer = _ref$viewer === void 0 ? null : _ref$viewer,
                    pdfViewer = _ref.pdfViewer,
                    eventBus = _ref.eventBus,
                    _ref$contextMenuItems = _ref.contextMenuItems,
                    contextMenuItems = _ref$contextMenuItems === void 0 ? null : _ref$contextMenuItems;

                _classCallCheck(this, PDFPresentationMode);

                this.container = container;
                this.viewer = viewer || container.firstElementChild;
                this.pdfViewer = pdfViewer;
                this.eventBus = eventBus;
                this.active = false;
                this.args = null;
                this.contextMenuOpen = false;
                this.mouseScrollTimeStamp = 0;
                this.mouseScrollDelta = 0;
                this.touchSwipeState = null;

                if (contextMenuItems) {
                  contextMenuItems.contextFirstPage.addEventListener('click', function () {
                    _this.contextMenuOpen = false;

                    _this.eventBus.dispatch('firstpage', {
                      source: _this
                    });
                  });
                  contextMenuItems.contextLastPage.addEventListener('click', function () {
                    _this.contextMenuOpen = false;

                    _this.eventBus.dispatch('lastpage', {
                      source: _this
                    });
                  });
                  contextMenuItems.contextPageRotateCw.addEventListener('click', function () {
                    _this.contextMenuOpen = false;

                    _this.eventBus.dispatch('rotatecw', {
                      source: _this
                    });
                  });
                  contextMenuItems.contextPageRotateCcw.addEventListener('click', function () {
                    _this.contextMenuOpen = false;

                    _this.eventBus.dispatch('rotateccw', {
                      source: _this
                    });
                  });
                }
              }

              _createClass(PDFPresentationMode, [{
                key: "request",
                value: function request() {
                  if (this.switchInProgress || this.active || !this.viewer.hasChildNodes()) {
                    return false;
                  }

                  this._addFullscreenChangeListeners();

                  this._setSwitchInProgress();

                  this._notifyStateChange();

                  if (this.container.requestFullscreen) {
                    this.container.requestFullscreen();
                  } else if (this.container.mozRequestFullScreen) {
                    this.container.mozRequestFullScreen();
                  } else if (this.container.webkitRequestFullscreen) {
                    this.container.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
                  } else if (this.container.msRequestFullscreen) {
                    this.container.msRequestFullscreen();
                  } else {
                    return false;
                  }

                  this.args = {
                    page: this.pdfViewer.currentPageNumber,
                    previousScale: this.pdfViewer.currentScaleValue
                  };
                  return true;
                }
              }, {
                key: "_mouseWheel",
                value: function _mouseWheel(evt) {
                  if (!this.active) {
                    return;
                  }

                  evt.preventDefault();
                  const delta = (0, _ui_utils.normalizeWheelEventDelta)(evt);
                  const currentTime = new Date().getTime();
                  const storedTime = this.mouseScrollTimeStamp;

                  if (currentTime > storedTime && currentTime - storedTime < MOUSE_SCROLL_COOLDOWN_TIME) {
                    return;
                  }

                  if (this.mouseScrollDelta > 0 && delta < 0 || this.mouseScrollDelta < 0 && delta > 0) {
                    this._resetMouseScrollState();
                  }

                  this.mouseScrollDelta += delta;

                  if (Math.abs(this.mouseScrollDelta) >= PAGE_SWITCH_THRESHOLD) {
                    const totalDelta = this.mouseScrollDelta;

                    this._resetMouseScrollState();

                    const success = totalDelta > 0 ? this._goToPreviousPage() : this._goToNextPage();

                    if (success) {
                      this.mouseScrollTimeStamp = currentTime;
                    }
                  }
                }
              }, {
                key: "_goToPreviousPage",
                value: function _goToPreviousPage() {
                  const page = this.pdfViewer.currentPageNumber;

                  if (page <= 1) {
                    return false;
                  }

                  this.pdfViewer.currentPageNumber = page - 1;
                  return true;
                }
              }, {
                key: "_goToNextPage",
                value: function _goToNextPage() {
                  const page = this.pdfViewer.currentPageNumber;

                  if (page >= this.pdfViewer.pagesCount) {
                    return false;
                  }

                  this.pdfViewer.currentPageNumber = page + 1;
                  return true;
                }
              }, {
                key: "_notifyStateChange",
                value: function _notifyStateChange() {
                  this.eventBus.dispatch('presentationmodechanged', {
                    source: this,
                    active: this.active,
                    switchInProgress: !!this.switchInProgress
                  });
                }
              }, {
                key: "_setSwitchInProgress",
                value: function _setSwitchInProgress() {
                  const _this2 = this;

                  if (this.switchInProgress) {
                    clearTimeout(this.switchInProgress);
                  }

                  this.switchInProgress = setTimeout(function () {
                    _this2._removeFullscreenChangeListeners();

                    delete _this2.switchInProgress;

                    _this2._notifyStateChange();
                  }, DELAY_BEFORE_RESETTING_SWITCH_IN_PROGRESS);
                }
              }, {
                key: "_resetSwitchInProgress",
                value: function _resetSwitchInProgress() {
                  if (this.switchInProgress) {
                    clearTimeout(this.switchInProgress);
                    delete this.switchInProgress;
                  }
                }
              }, {
                key: "_enter",
                value: function _enter() {
                  const _this3 = this;

                  this.active = true;

                  this._resetSwitchInProgress();

                  this._notifyStateChange();

                  this.container.classList.add(ACTIVE_SELECTOR);
                  setTimeout(function () {
                    _this3.pdfViewer.currentPageNumber = _this3.args.page;
                    _this3.pdfViewer.currentScaleValue = 'page-fit';
                  }, 0);

                  this._addWindowListeners();

                  this._showControls();

                  this.contextMenuOpen = false;
                  this.container.setAttribute('contextmenu', 'viewerContextMenu');
                  window.getSelection().removeAllRanges();
                }
              }, {
                key: "_exit",
                value: function _exit() {
                  const _this4 = this;

                  const page = this.pdfViewer.currentPageNumber;
                  this.container.classList.remove(ACTIVE_SELECTOR);
                  setTimeout(function () {
                    _this4.active = false;

                    _this4._removeFullscreenChangeListeners();

                    _this4._notifyStateChange();

                    _this4.pdfViewer.currentScaleValue = _this4.args.previousScale;
                    _this4.pdfViewer.currentPageNumber = page;
                    _this4.args = null;
                  }, 0);

                  this._removeWindowListeners();

                  this._hideControls();

                  this._resetMouseScrollState();

                  this.container.removeAttribute('contextmenu');
                  this.contextMenuOpen = false;
                }
              }, {
                key: "_mouseDown",
                value: function _mouseDown(evt) {
                  if (this.contextMenuOpen) {
                    this.contextMenuOpen = false;
                    evt.preventDefault();
                    return;
                  }

                  if (evt.button === 0) {
                    const isInternalLink = evt.target.href && evt.target.classList.contains('internalLink');

                    if (!isInternalLink) {
                      evt.preventDefault();

                      if (evt.shiftKey) {
                        this._goToPreviousPage();
                      } else {
                        this._goToNextPage();
                      }
                    }
                  }
                }
              }, {
                key: "_contextMenu",
                value: function _contextMenu() {
                  this.contextMenuOpen = true;
                }
              }, {
                key: "_showControls",
                value: function _showControls() {
                  const _this5 = this;

                  if (this.controlsTimeout) {
                    clearTimeout(this.controlsTimeout);
                  } else {
                    this.container.classList.add(CONTROLS_SELECTOR);
                  }

                  this.controlsTimeout = setTimeout(function () {
                    _this5.container.classList.remove(CONTROLS_SELECTOR);

                    delete _this5.controlsTimeout;
                  }, DELAY_BEFORE_HIDING_CONTROLS);
                }
              }, {
                key: "_hideControls",
                value: function _hideControls() {
                  if (!this.controlsTimeout) {
                    return;
                  }

                  clearTimeout(this.controlsTimeout);
                  this.container.classList.remove(CONTROLS_SELECTOR);
                  delete this.controlsTimeout;
                }
              }, {
                key: "_resetMouseScrollState",
                value: function _resetMouseScrollState() {
                  this.mouseScrollTimeStamp = 0;
                  this.mouseScrollDelta = 0;
                }
              }, {
                key: "_touchSwipe",
                value: function _touchSwipe(evt) {
                  if (!this.active) {
                    return;
                  }

                  if (evt.touches.length > 1) {
                    this.touchSwipeState = null;
                    return;
                  }

                  switch (evt.type) {
                    case 'touchstart':
                      this.touchSwipeState = {
                        startX: evt.touches[0].pageX,
                        startY: evt.touches[0].pageY,
                        endX: evt.touches[0].pageX,
                        endY: evt.touches[0].pageY
                      };
                      break;

                    case 'touchmove':
                      if (this.touchSwipeState === null) {
                        return;
                      }

                      this.touchSwipeState.endX = evt.touches[0].pageX;
                      this.touchSwipeState.endY = evt.touches[0].pageY;
                      evt.preventDefault();
                      break;

                    case 'touchend':
                      if (this.touchSwipeState === null) {
                        return;
                      }

                      let delta = 0;
                      const dx = this.touchSwipeState.endX - this.touchSwipeState.startX;
                      const dy = this.touchSwipeState.endY - this.touchSwipeState.startY;
                      const absAngle = Math.abs(Math.atan2(dy, dx));

                      if (Math.abs(dx) > SWIPE_MIN_DISTANCE_THRESHOLD && (absAngle <= SWIPE_ANGLE_THRESHOLD || absAngle >= Math.PI - SWIPE_ANGLE_THRESHOLD)) {
                        delta = dx;
                      } else if (Math.abs(dy) > SWIPE_MIN_DISTANCE_THRESHOLD && Math.abs(absAngle - Math.PI / 2) <= SWIPE_ANGLE_THRESHOLD) {
                        delta = dy;
                      }

                      if (delta > 0) {
                        this._goToPreviousPage();
                      } else if (delta < 0) {
                        this._goToNextPage();
                      }

                      break;
                  }
                }
              }, {
                key: "_addWindowListeners",
                value: function _addWindowListeners() {
                  this.showControlsBind = this._showControls.bind(this);
                  this.mouseDownBind = this._mouseDown.bind(this);
                  this.mouseWheelBind = this._mouseWheel.bind(this);
                  this.resetMouseScrollStateBind = this._resetMouseScrollState.bind(this);
                  this.contextMenuBind = this._contextMenu.bind(this);
                  this.touchSwipeBind = this._touchSwipe.bind(this);
                  window.addEventListener('mousemove', this.showControlsBind);
                  window.addEventListener('mousedown', this.mouseDownBind);
                  window.addEventListener('wheel', this.mouseWheelBind);
                  window.addEventListener('keydown', this.resetMouseScrollStateBind);
                  window.addEventListener('contextmenu', this.contextMenuBind);
                  window.addEventListener('touchstart', this.touchSwipeBind);
                  window.addEventListener('touchmove', this.touchSwipeBind);
                  window.addEventListener('touchend', this.touchSwipeBind);
                }
              }, {
                key: "_removeWindowListeners",
                value: function _removeWindowListeners() {
                  window.removeEventListener('mousemove', this.showControlsBind);
                  window.removeEventListener('mousedown', this.mouseDownBind);
                  window.removeEventListener('wheel', this.mouseWheelBind);
                  window.removeEventListener('keydown', this.resetMouseScrollStateBind);
                  window.removeEventListener('contextmenu', this.contextMenuBind);
                  window.removeEventListener('touchstart', this.touchSwipeBind);
                  window.removeEventListener('touchmove', this.touchSwipeBind);
                  window.removeEventListener('touchend', this.touchSwipeBind);
                  delete this.showControlsBind;
                  delete this.mouseDownBind;
                  delete this.mouseWheelBind;
                  delete this.resetMouseScrollStateBind;
                  delete this.contextMenuBind;
                  delete this.touchSwipeBind;
                }
              }, {
                key: "_fullscreenChange",
                value: function _fullscreenChange() {
                  if (this.isFullscreen) {
                    this._enter();
                  } else {
                    this._exit();
                  }
                }
              }, {
                key: "_addFullscreenChangeListeners",
                value: function _addFullscreenChangeListeners() {
                  this.fullscreenChangeBind = this._fullscreenChange.bind(this);
                  window.addEventListener('fullscreenchange', this.fullscreenChangeBind);
                  window.addEventListener('mozfullscreenchange', this.fullscreenChangeBind);
                  window.addEventListener('webkitfullscreenchange', this.fullscreenChangeBind);
                  window.addEventListener('MSFullscreenChange', this.fullscreenChangeBind);
                }
              }, {
                key: "_removeFullscreenChangeListeners",
                value: function _removeFullscreenChangeListeners() {
                  window.removeEventListener('fullscreenchange', this.fullscreenChangeBind);
                  window.removeEventListener('mozfullscreenchange', this.fullscreenChangeBind);
                  window.removeEventListener('webkitfullscreenchange', this.fullscreenChangeBind);
                  window.removeEventListener('MSFullscreenChange', this.fullscreenChangeBind);
                  delete this.fullscreenChangeBind;
                }
              }, {
                key: "isFullscreen",
                get: function get() {
                  return !!(document.fullscreenElement || document.mozFullScreen || document.webkitIsFullScreen || document.msFullscreenElement);
                }
              }]);

              return PDFPresentationMode;
            }();

        exports.PDFPresentationMode = PDFPresentationMode;

/***/ }),
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFSidebarResizer = void 0;

        const _ui_utils = __webpack_require__(5);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const SIDEBAR_WIDTH_VAR = '--sidebar-width';
        const SIDEBAR_MIN_WIDTH = 200;
        const SIDEBAR_RESIZING_CLASS = 'sidebarResizing';

        const PDFSidebarResizer =
            /*#__PURE__*/
            function () {
              function PDFSidebarResizer(options, eventBus) {
                const _this = this;

                const l10n = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : _ui_utils.NullL10n;

                _classCallCheck(this, PDFSidebarResizer);

                this.enabled = false;
                this.isRTL = false;
                this.sidebarOpen = false;
                this.doc = document.documentElement;
                this._width = null;
                this._outerContainerWidth = null;
                this._boundEvents = Object.create(null);
                this.outerContainer = options.outerContainer;
                this.resizer = options.resizer;
                this.eventBus = eventBus;
                this.l10n = l10n;

                if (typeof CSS === 'undefined' || typeof CSS.supports !== 'function' || !CSS.supports(SIDEBAR_WIDTH_VAR, "calc(-1 * ".concat(SIDEBAR_MIN_WIDTH, "px)"))) {
                  console.warn('PDFSidebarResizer: ' + 'The browser does not support resizing of the sidebar.');
                  return;
                }

                this.enabled = true;
                this.resizer.classList.remove('hidden');
                this.l10n.getDirection().then(function (dir) {
                  _this.isRTL = dir === 'rtl';
                });

                this._addEventListeners();
              }

              _createClass(PDFSidebarResizer, [{
                key: "_updateWidth",
                value: function _updateWidth() {
                  let width = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;

                  if (!this.enabled) {
                    return false;
                  }

                  const maxWidth = Math.floor(this.outerContainerWidth / 2);

                  if (width > maxWidth) {
                    width = maxWidth;
                  }

                  if (width < SIDEBAR_MIN_WIDTH) {
                    width = SIDEBAR_MIN_WIDTH;
                  }

                  if (width === this._width) {
                    return false;
                  }

                  this._width = width;
                  this.doc.style.setProperty(SIDEBAR_WIDTH_VAR, "".concat(width, "px"));
                  return true;
                }
              }, {
                key: "_mouseMove",
                value: function _mouseMove(evt) {
                  let width = evt.clientX;

                  if (this.isRTL) {
                    width = this.outerContainerWidth - width;
                  }

                  this._updateWidth(width);
                }
              }, {
                key: "_mouseUp",
                value: function _mouseUp(evt) {
                  this.outerContainer.classList.remove(SIDEBAR_RESIZING_CLASS);
                  this.eventBus.dispatch('resize', {
                    source: this
                  });
                  const _boundEvents = this._boundEvents;
                  window.removeEventListener('mousemove', _boundEvents.mouseMove);
                  window.removeEventListener('mouseup', _boundEvents.mouseUp);
                }
              }, {
                key: "_addEventListeners",
                value: function _addEventListeners() {
                  const _this2 = this;

                  if (!this.enabled) {
                    return;
                  }

                  const _boundEvents = this._boundEvents;
                  _boundEvents.mouseMove = this._mouseMove.bind(this);
                  _boundEvents.mouseUp = this._mouseUp.bind(this);
                  this.resizer.addEventListener('mousedown', function (evt) {
                    if (evt.button !== 0) {
                      return;
                    }

                    _this2.outerContainer.classList.add(SIDEBAR_RESIZING_CLASS);

                    window.addEventListener('mousemove', _boundEvents.mouseMove);
                    window.addEventListener('mouseup', _boundEvents.mouseUp);
                  });
                  this.eventBus.on('sidebarviewchanged', function (evt) {
                    _this2.sidebarOpen = !!(evt && evt.view);
                  });
                  this.eventBus.on('resize', function (evt) {
                    if (evt && evt.source === window) {
                      _this2._outerContainerWidth = null;

                      if (_this2._width) {
                        if (_this2.sidebarOpen) {
                          _this2.outerContainer.classList.add(SIDEBAR_RESIZING_CLASS);

                          const updated = _this2._updateWidth(_this2._width);

                          Promise.resolve().then(function () {
                            _this2.outerContainer.classList.remove(SIDEBAR_RESIZING_CLASS);

                            if (updated) {
                              _this2.eventBus.dispatch('resize', {
                                source: _this2
                              });
                            }
                          });
                        } else {
                          _this2._updateWidth(_this2._width);
                        }
                      }
                    }
                  });
                }
              }, {
                key: "outerContainerWidth",
                get: function get() {
                  if (!this._outerContainerWidth) {
                    this._outerContainerWidth = this.outerContainer.clientWidth;
                  }

                  return this._outerContainerWidth;
                }
              }]);

              return PDFSidebarResizer;
            }();

        exports.PDFSidebarResizer = PDFSidebarResizer;

/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFThumbnailViewer = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdf_thumbnail_view = __webpack_require__(26);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const THUMBNAIL_SCROLL_MARGIN = -19;
        const THUMBNAIL_SELECTED_CLASS = 'selected';

        const PDFThumbnailViewer =
            /*#__PURE__*/
            function () {
              function PDFThumbnailViewer(_ref) {
                const container = _ref.container,
                    linkService = _ref.linkService,
                    renderingQueue = _ref.renderingQueue,
                    _ref$l10n = _ref.l10n,
                    l10n = _ref$l10n === void 0 ? _ui_utils.NullL10n : _ref$l10n;

                _classCallCheck(this, PDFThumbnailViewer);

                this.container = container;
                this.linkService = linkService;
                this.renderingQueue = renderingQueue;
                this.l10n = l10n;
                this.scroll = (0, _ui_utils.watchScroll)(this.container, this._scrollUpdated.bind(this));

                this._resetView();
              }

              _createClass(PDFThumbnailViewer, [{
                key: "_scrollUpdated",
                value: function _scrollUpdated() {
                  this.renderingQueue.renderHighestPriority();
                }
              }, {
                key: "getThumbnail",
                value: function getThumbnail(index) {
                  return this._thumbnails[index];
                }
              }, {
                key: "_getVisibleThumbs",
                value: function _getVisibleThumbs() {
                  return (0, _ui_utils.getVisibleElements)(this.container, this._thumbnails);
                }
              }, {
                key: "scrollThumbnailIntoView",
                value: function scrollThumbnailIntoView(pageNumber) {
                  if (!this.pdfDocument) {
                    return;
                  }

                  const thumbnailView = this._thumbnails[pageNumber - 1];

                  if (!thumbnailView) {
                    console.error('scrollThumbnailIntoView: Invalid "pageNumber" parameter.');
                    return;
                  }

                  if (pageNumber !== this._currentPageNumber) {
                    const prevThumbnailView = this._thumbnails[this._currentPageNumber - 1];
                    prevThumbnailView.div.classList.remove(THUMBNAIL_SELECTED_CLASS);
                    thumbnailView.div.classList.add(THUMBNAIL_SELECTED_CLASS);
                  }

                  const visibleThumbs = this._getVisibleThumbs();

                  const numVisibleThumbs = visibleThumbs.views.length;

                  if (numVisibleThumbs > 0) {
                    const first = visibleThumbs.first.id;
                    const last = numVisibleThumbs > 1 ? visibleThumbs.last.id : first;
                    let shouldScroll = false;

                    if (pageNumber <= first || pageNumber >= last) {
                      shouldScroll = true;
                    } else {
                      visibleThumbs.views.some(function (view) {
                        if (view.id !== pageNumber) {
                          return false;
                        }

                        shouldScroll = view.percent < 100;
                        return true;
                      });
                    }

                    if (shouldScroll) {
                      (0, _ui_utils.scrollIntoView)(thumbnailView.div, {
                        top: THUMBNAIL_SCROLL_MARGIN
                      });
                    }
                  }

                  this._currentPageNumber = pageNumber;
                }
              }, {
                key: "cleanup",
                value: function cleanup() {
                  _pdf_thumbnail_view.PDFThumbnailView.cleanup();
                }
              }, {
                key: "_resetView",
                value: function _resetView() {
                  this._thumbnails = [];
                  this._currentPageNumber = 1;
                  this._pageLabels = null;
                  this._pagesRotation = 0;
                  this._pagesRequests = [];
                  this.container.textContent = '';
                }
              }, {
                key: "setDocument",
                value: function setDocument(pdfDocument) {
                  const _this = this;

                  if (this.pdfDocument) {
                    this._cancelRendering();

                    this._resetView();
                  }

                  this.pdfDocument = pdfDocument;

                  if (!pdfDocument) {
                    return;
                  }

                  pdfDocument.getPage(1).then(function (firstPage) {
                    const pagesCount = pdfDocument.numPages;
                    const viewport = firstPage.getViewport({
                      scale: 1
                    });

                    for (let pageNum = 1; pageNum <= pagesCount; ++pageNum) {
                      const thumbnail = new _pdf_thumbnail_view.PDFThumbnailView({
                        container: _this.container,
                        id: pageNum,
                        defaultViewport: viewport.clone(),
                        linkService: _this.linkService,
                        renderingQueue: _this.renderingQueue,
                        disableCanvasToImageConversion: false,
                        l10n: _this.l10n
                      });

                      _this._thumbnails.push(thumbnail);
                    }

                    const thumbnailView = _this._thumbnails[_this._currentPageNumber - 1];
                    thumbnailView.div.classList.add(THUMBNAIL_SELECTED_CLASS);
                  })["catch"](function (reason) {
                    console.error('Unable to initialize thumbnail viewer', reason);
                  });
                }
              }, {
                key: "_cancelRendering",
                value: function _cancelRendering() {
                  let i = 0;
                  const ii = this._thumbnails.length;
                  for (; i < ii; i++) {
                    if (this._thumbnails[i]) {
                      this._thumbnails[i].cancelRendering();
                    }
                  }
                }
              }, {
                key: "setPageLabels",
                value: function setPageLabels(labels) {
                  if (!this.pdfDocument) {
                    return;
                  }

                  if (!labels) {
                    this._pageLabels = null;
                  } else if (!(Array.isArray(labels) && this.pdfDocument.numPages === labels.length)) {
                    this._pageLabels = null;
                    console.error('PDFThumbnailViewer_setPageLabels: Invalid page labels.');
                  } else {
                    this._pageLabels = labels;
                  }

                  let i = 0;
                  const ii = this._thumbnails.length;
                  for (; i < ii; i++) {
                    const label = this._pageLabels && this._pageLabels[i];

                    this._thumbnails[i].setPageLabel(label);
                  }
                }
              }, {
                key: "_ensurePdfPageLoaded",
                value: function _ensurePdfPageLoaded(thumbView) {
                  const _this2 = this;

                  if (thumbView.pdfPage) {
                    return Promise.resolve(thumbView.pdfPage);
                  }

                  const pageNumber = thumbView.id;

                  if (this._pagesRequests[pageNumber]) {
                    return this._pagesRequests[pageNumber];
                  }

                  const promise = this.pdfDocument.getPage(pageNumber).then(function (pdfPage) {
                    thumbView.setPdfPage(pdfPage);
                    _this2._pagesRequests[pageNumber] = null;
                    return pdfPage;
                  })["catch"](function (reason) {
                    console.error('Unable to get page for thumb view', reason);
                    _this2._pagesRequests[pageNumber] = null;
                  });
                  this._pagesRequests[pageNumber] = promise;
                  return promise;
                }
              }, {
                key: "forceRendering",
                value: function forceRendering() {
                  const _this3 = this;

                  const visibleThumbs = this._getVisibleThumbs();

                  const thumbView = this.renderingQueue.getHighestPriority(visibleThumbs, this._thumbnails, this.scroll.down);

                  if (thumbView) {
                    this._ensurePdfPageLoaded(thumbView).then(function () {
                      _this3.renderingQueue.renderView(thumbView);
                    });

                    return true;
                  }

                  return false;
                }
              }, {
                key: "pagesRotation",
                get: function get() {
                  return this._pagesRotation;
                },
                set: function set(rotation) {
                  if (!(0, _ui_utils.isValidRotation)(rotation)) {
                    throw new Error('Invalid thumbnails rotation angle.');
                  }

                  if (!this.pdfDocument) {
                    return;
                  }

                  if (this._pagesRotation === rotation) {
                    return;
                  }

                  this._pagesRotation = rotation;

                  let i = 0;
                  const ii = this._thumbnails.length;
                  for (; i < ii; i++) {
                    this._thumbnails[i].update(rotation);
                  }
                }
              }]);

              return PDFThumbnailViewer;
            }();

        exports.PDFThumbnailViewer = PDFThumbnailViewer;

/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFThumbnailView = void 0;

        const _pdfjsLib = __webpack_require__(7);

        const _ui_utils = __webpack_require__(5);

        const _pdf_rendering_queue = __webpack_require__(11);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const MAX_NUM_SCALING_STEPS = 3;
        const THUMBNAIL_CANVAS_BORDER_WIDTH = 1;
        const THUMBNAIL_WIDTH = 98;

        const TempImageFactory = function TempImageFactoryClosure() {
          let tempCanvasCache = null;
          return {
            getCanvas: function getCanvas(width, height) {
              let tempCanvas = tempCanvasCache;

              if (!tempCanvas) {
                tempCanvas = document.createElement('canvas');
                tempCanvasCache = tempCanvas;
              }

              tempCanvas.width = width;
              tempCanvas.height = height;
              tempCanvas.mozOpaque = true;
              const ctx = tempCanvas.getContext('2d', {
                alpha: false
              });
              ctx.save();
              ctx.fillStyle = 'rgb(255, 255, 255)';
              ctx.fillRect(0, 0, width, height);
              ctx.restore();
              return tempCanvas;
            },
            destroyCanvas: function destroyCanvas() {
              const tempCanvas = tempCanvasCache;

              if (tempCanvas) {
                tempCanvas.width = 0;
                tempCanvas.height = 0;
              }

              tempCanvasCache = null;
            }
          };
        }();

        const PDFThumbnailView =
            /*#__PURE__*/
            function () {
              function PDFThumbnailView(_ref) {
                const container = _ref.container,
                    id = _ref.id,
                    defaultViewport = _ref.defaultViewport,
                    linkService = _ref.linkService,
                    renderingQueue = _ref.renderingQueue,
                    _ref$disableCanvasToI = _ref.disableCanvasToImageConversion,
                    disableCanvasToImageConversion = _ref$disableCanvasToI === void 0 ? false : _ref$disableCanvasToI,
                    _ref$l10n = _ref.l10n,
                    l10n = _ref$l10n === void 0 ? _ui_utils.NullL10n : _ref$l10n;

                _classCallCheck(this, PDFThumbnailView);

                this.id = id;
                this.renderingId = 'thumbnail' + id;
                this.pageLabel = null;
                this.pdfPage = null;
                this.rotation = 0;
                this.viewport = defaultViewport;
                this.pdfPageRotate = defaultViewport.rotation;
                this.linkService = linkService;
                this.renderingQueue = renderingQueue;
                this.renderTask = null;
                this.renderingState = _pdf_rendering_queue.RenderingStates.INITIAL;
                this.resume = null;
                this.disableCanvasToImageConversion = disableCanvasToImageConversion;
                this.pageWidth = this.viewport.width;
                this.pageHeight = this.viewport.height;
                this.pageRatio = this.pageWidth / this.pageHeight;
                this.canvasWidth = THUMBNAIL_WIDTH;
                this.canvasHeight = this.canvasWidth / this.pageRatio | 0;
                this.scale = this.canvasWidth / this.pageWidth;
                this.l10n = l10n;
                const anchor = document.createElement('a');
                anchor.href = linkService.getAnchorUrl('#page=' + id);
                this.l10n.get('thumb_page_title', {
                  page: id
                }, 'Page {{page}}').then(function (msg) {
                  anchor.title = msg;
                });

                anchor.onclick = function () {
                  linkService.page = id;
                  return false;
                };

                this.anchor = anchor;
                const div = document.createElement('div');
                div.className = 'thumbnail';
                div.setAttribute('data-page-number', this.id);
                this.div = div;
                const ring = document.createElement('div');
                ring.className = 'thumbnailSelectionRing';
                const borderAdjustment = 2 * THUMBNAIL_CANVAS_BORDER_WIDTH;
                ring.style.width = this.canvasWidth + borderAdjustment + 'px';
                ring.style.height = this.canvasHeight + borderAdjustment + 'px';
                this.ring = ring;
                div.appendChild(ring);
                anchor.appendChild(div);
                container.appendChild(anchor);
              }

              _createClass(PDFThumbnailView, [{
                key: "setPdfPage",
                value: function setPdfPage(pdfPage) {
                  this.pdfPage = pdfPage;
                  this.pdfPageRotate = pdfPage.rotate;
                  const totalRotation = (this.rotation + this.pdfPageRotate) % 360;
                  this.viewport = pdfPage.getViewport({
                    scale: 1,
                    rotation: totalRotation
                  });
                  this.reset();
                }
              }, {
                key: "reset",
                value: function reset() {
                  this.cancelRendering();
                  this.renderingState = _pdf_rendering_queue.RenderingStates.INITIAL;
                  this.pageWidth = this.viewport.width;
                  this.pageHeight = this.viewport.height;
                  this.pageRatio = this.pageWidth / this.pageHeight;
                  this.canvasHeight = this.canvasWidth / this.pageRatio | 0;
                  this.scale = this.canvasWidth / this.pageWidth;
                  this.div.removeAttribute('data-loaded');
                  const ring = this.ring;
                  const childNodes = ring.childNodes;

                  for (let i = childNodes.length - 1; i >= 0; i--) {
                    ring.removeChild(childNodes[i]);
                  }

                  const borderAdjustment = 2 * THUMBNAIL_CANVAS_BORDER_WIDTH;
                  ring.style.width = this.canvasWidth + borderAdjustment + 'px';
                  ring.style.height = this.canvasHeight + borderAdjustment + 'px';

                  if (this.canvas) {
                    this.canvas.width = 0;
                    this.canvas.height = 0;
                    delete this.canvas;
                  }

                  if (this.image) {
                    this.image.removeAttribute('src');
                    delete this.image;
                  }
                }
              }, {
                key: "update",
                value: function update(rotation) {
                  if (typeof rotation !== 'undefined') {
                    this.rotation = rotation;
                  }

                  const totalRotation = (this.rotation + this.pdfPageRotate) % 360;
                  this.viewport = this.viewport.clone({
                    scale: 1,
                    rotation: totalRotation
                  });
                  this.reset();
                }
              }, {
                key: "cancelRendering",
                value: function cancelRendering() {
                  if (this.renderTask) {
                    this.renderTask.cancel();
                    this.renderTask = null;
                  }

                  this.resume = null;
                }
              }, {
                key: "_getPageDrawContext",
                value: function _getPageDrawContext() {
                  const noCtxScale = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
                  const canvas = document.createElement('canvas');
                  this.canvas = canvas;
                  canvas.mozOpaque = true;
                  const ctx = canvas.getContext('2d', {
                    alpha: false
                  });
                  const outputScale = (0, _ui_utils.getOutputScale)(ctx);
                  canvas.width = this.canvasWidth * outputScale.sx | 0;
                  canvas.height = this.canvasHeight * outputScale.sy | 0;
                  canvas.style.width = this.canvasWidth + 'px';
                  canvas.style.height = this.canvasHeight + 'px';

                  if (!noCtxScale && outputScale.scaled) {
                    ctx.scale(outputScale.sx, outputScale.sy);
                  }

                  return ctx;
                }
              }, {
                key: "_convertCanvasToImage",
                value: function _convertCanvasToImage() {
                  const _this = this;

                  if (!this.canvas) {
                    return;
                  }

                  if (this.renderingState !== _pdf_rendering_queue.RenderingStates.FINISHED) {
                    return;
                  }

                  const id = this.renderingId;
                  const className = 'thumbnailImage';

                  if (this.disableCanvasToImageConversion) {
                    this.canvas.id = id;
                    this.canvas.className = className;
                    this.l10n.get('thumb_page_canvas', {
                      page: this.pageId
                    }, 'Thumbnail of Page {{page}}').then(function (msg) {
                      _this.canvas.setAttribute('aria-label', msg);
                    });
                    this.div.setAttribute('data-loaded', true);
                    this.ring.appendChild(this.canvas);
                    return;
                  }

                  const image = document.createElement('img');
                  image.id = id;
                  image.className = className;
                  this.l10n.get('thumb_page_canvas', {
                    page: this.pageId
                  }, 'Thumbnail of Page {{page}}').then(function (msg) {
                    image.setAttribute('aria-label', msg);
                  });
                  image.style.width = this.canvasWidth + 'px';
                  image.style.height = this.canvasHeight + 'px';
                  image.src = this.canvas.toDataURL();
                  this.image = image;
                  this.div.setAttribute('data-loaded', true);
                  this.ring.appendChild(image);
                  this.canvas.width = 0;
                  this.canvas.height = 0;
                  delete this.canvas;
                }
              }, {
                key: "draw",
                value: function draw() {
                  const _this2 = this;

                  if (this.renderingState !== _pdf_rendering_queue.RenderingStates.INITIAL) {
                    console.error('Must be in new state before drawing');
                    return Promise.resolve(undefined);
                  }

                  this.renderingState = _pdf_rendering_queue.RenderingStates.RUNNING;
                  const renderCapability = (0, _pdfjsLib.createPromiseCapability)();

                  const finishRenderTask = function finishRenderTask(error) {
                    if (renderTask === _this2.renderTask) {
                      _this2.renderTask = null;
                    }

                    if (error instanceof _pdfjsLib.RenderingCancelledException) {
                      renderCapability.resolve(undefined);
                      return;
                    }

                    _this2.renderingState = _pdf_rendering_queue.RenderingStates.FINISHED;

                    _this2._convertCanvasToImage();

                    if (!error) {
                      renderCapability.resolve(undefined);
                    } else {
                      renderCapability.reject(error);
                    }
                  };

                  const ctx = this._getPageDrawContext();

                  const drawViewport = this.viewport.clone({
                    scale: this.scale
                  });

                  const renderContinueCallback = function renderContinueCallback(cont) {
                    if (!_this2.renderingQueue.isHighestPriority(_this2)) {
                      _this2.renderingState = _pdf_rendering_queue.RenderingStates.PAUSED;

                      _this2.resume = function () {
                        _this2.renderingState = _pdf_rendering_queue.RenderingStates.RUNNING;
                        cont();
                      };

                      return;
                    }

                    cont();
                  };

                  const renderContext = {
                    canvasContext: ctx,
                    viewport: drawViewport
                  };
                  var renderTask = this.renderTask = this.pdfPage.render(renderContext);
                  renderTask.onContinue = renderContinueCallback;
                  renderTask.promise.then(function () {
                    finishRenderTask(null);
                  }, function (error) {
                    finishRenderTask(error);
                  });
                  return renderCapability.promise;
                }
              }, {
                key: "setImage",
                value: function setImage(pageView) {
                  if (this.renderingState !== _pdf_rendering_queue.RenderingStates.INITIAL) {
                    return;
                  }

                  const img = pageView.canvas;

                  if (!img) {
                    return;
                  }

                  if (!this.pdfPage) {
                    this.setPdfPage(pageView.pdfPage);
                  }

                  this.renderingState = _pdf_rendering_queue.RenderingStates.FINISHED;

                  const ctx = this._getPageDrawContext(true);

                  const canvas = ctx.canvas;

                  if (img.width <= 2 * canvas.width) {
                    ctx.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, canvas.height);

                    this._convertCanvasToImage();

                    return;
                  }

                  let reducedWidth = canvas.width << MAX_NUM_SCALING_STEPS;
                  let reducedHeight = canvas.height << MAX_NUM_SCALING_STEPS;
                  const reducedImage = TempImageFactory.getCanvas(reducedWidth, reducedHeight);
                  const reducedImageCtx = reducedImage.getContext('2d');

                  while (reducedWidth > img.width || reducedHeight > img.height) {
                    reducedWidth >>= 1;
                    reducedHeight >>= 1;
                  }

                  reducedImageCtx.drawImage(img, 0, 0, img.width, img.height, 0, 0, reducedWidth, reducedHeight);

                  while (reducedWidth > 2 * canvas.width) {
                    reducedImageCtx.drawImage(reducedImage, 0, 0, reducedWidth, reducedHeight, 0, 0, reducedWidth >> 1, reducedHeight >> 1);
                    reducedWidth >>= 1;
                    reducedHeight >>= 1;
                  }

                  ctx.drawImage(reducedImage, 0, 0, reducedWidth, reducedHeight, 0, 0, canvas.width, canvas.height);

                  this._convertCanvasToImage();
                }
              }, {
                key: "setPageLabel",
                value: function setPageLabel(label) {
                  const _this3 = this;

                  this.pageLabel = typeof label === 'string' ? label : null;
                  this.l10n.get('thumb_page_title', {
                    page: this.pageId
                  }, 'Page {{page}}').then(function (msg) {
                    _this3.anchor.title = msg;
                  });

                  if (this.renderingState !== _pdf_rendering_queue.RenderingStates.FINISHED) {
                    return;
                  }

                  this.l10n.get('thumb_page_canvas', {
                    page: this.pageId
                  }, 'Thumbnail of Page {{page}}').then(function (ariaLabel) {
                    if (_this3.image) {
                      _this3.image.setAttribute('aria-label', ariaLabel);
                    } else if (_this3.disableCanvasToImageConversion && _this3.canvas) {
                      _this3.canvas.setAttribute('aria-label', ariaLabel);
                    }
                  });
                }
              }, {
                key: "pageId",
                get: function get() {
                  return this.pageLabel !== null ? this.pageLabel : this.id;
                }
              }], [{
                key: "cleanup",
                value: function cleanup() {
                  TempImageFactory.destroyCanvas();
                }
              }]);

              return PDFThumbnailView;
            }();

        exports.PDFThumbnailView = PDFThumbnailView;

/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFViewer = void 0;

        const _base_viewer = __webpack_require__(28);

        const _pdfjsLib = __webpack_require__(7);

        function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _get(target, property, receiver) { if (typeof Reflect !== "undefined" && Reflect.get) { _get = Reflect.get; } else { _get = function _get(target, property, receiver) {
  const base = _superPropBase(target, property);
  if (!base) return;
  const desc = Object.getOwnPropertyDescriptor(base, property);
  if (desc.get) { return desc.get.call(receiver); } return desc.value; }; } return _get(target, property, receiver || target); }

function _superPropBase(object, property) { while (!Object.prototype.hasOwnProperty.call(object, property)) { object = _getPrototypeOf(object); if (object === null) break; } return object; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

        const PDFViewer =
            /*#__PURE__*/
            function (_BaseViewer) {
              _inherits(PDFViewer, _BaseViewer);

              function PDFViewer() {
                _classCallCheck(this, PDFViewer);

                return _possibleConstructorReturn(this, _getPrototypeOf(PDFViewer).apply(this, arguments));
              }

              _createClass(PDFViewer, [{
                key: "_scrollIntoView",
                value: function _scrollIntoView(_ref) {
                  const pageDiv = _ref.pageDiv,
                      _ref$pageSpot = _ref.pageSpot;
                  let pageSpot = _ref$pageSpot === void 0 ? null : _ref$pageSpot;
                  const _ref$pageNumber = _ref.pageNumber,
                      pageNumber = _ref$pageNumber === void 0 ? null : _ref$pageNumber;

                  if (!pageSpot && !this.isInPresentationMode) {
                    const left = pageDiv.offsetLeft + pageDiv.clientLeft;
                    const right = left + pageDiv.clientWidth;
                    const _this$container = this.container,
                        scrollLeft = _this$container.scrollLeft,
                        clientWidth = _this$container.clientWidth;

                    if (this._isScrollModeHorizontal || left < scrollLeft || right > scrollLeft + clientWidth) {
                      pageSpot = {
                        left: 0,
                        top: 0
                      };
                    }
                  }

                  _get(_getPrototypeOf(PDFViewer.prototype), "_scrollIntoView", this).call(this, {
                    pageDiv: pageDiv,
                    pageSpot: pageSpot,
                    pageNumber: pageNumber
                  });
                }
              }, {
                key: "_getVisiblePages",
                value: function _getVisiblePages() {
                  if (this.isInPresentationMode) {
                    return this._getCurrentVisiblePage();
                  }

                  return _get(_getPrototypeOf(PDFViewer.prototype), "_getVisiblePages", this).call(this);
                }
              }, {
                key: "_updateHelper",
                value: function _updateHelper(visiblePages) {
                  if (this.isInPresentationMode) {
                    return;
                  }

                  let currentId = this._currentPageNumber;
                  let stillFullyVisible = false;
                  let _iteratorNormalCompletion = true;
                  let _didIteratorError = false;
                  let _iteratorError = undefined;

                  try {
                    for (var _iterator = visiblePages[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                      const page = _step.value;

                      if (page.percent < 100) {
                        break;
                      }

                      if (page.id === currentId) {
                        stillFullyVisible = true;
                        break;
                      }
                    }
                  } catch (err) {
                    _didIteratorError = true;
                    _iteratorError = err;
                  } finally {
                    try {
                      if (!_iteratorNormalCompletion && _iterator["return"] != null) {
                        _iterator["return"]();
                      }
                    } finally {
                      if (_didIteratorError) {
                        throw _iteratorError;
                      }
                    }
                  }

                  if (!stillFullyVisible) {
                    currentId = visiblePages[0].id;
                  }

                  this._setCurrentPageNumber(currentId);
                }
              }, {
                key: "_setDocumentViewerElement",
                get: function get() {
                  return (0, _pdfjsLib.shadow)(this, '_setDocumentViewerElement', this.viewer);
                }
              }]);

              return PDFViewer;
            }(_base_viewer.BaseViewer);

        exports.PDFViewer = PDFViewer;

/***/ }),
/* 28 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.BaseViewer = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdf_rendering_queue = __webpack_require__(11);

        const _annotation_layer_builder = __webpack_require__(29);

        const _pdfjsLib = __webpack_require__(7);

        const _pdf_page_view = __webpack_require__(30);

        const _pdf_link_service = __webpack_require__(21);

        const _text_layer_builder = __webpack_require__(31);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const DEFAULT_CACHE_SIZE = 10;

        function PDFPageViewBuffer(size) {
  const data = [];

  this.push = function (view) {
    const i = data.indexOf(view);

    if (i >= 0) {
      data.splice(i, 1);
    }

    data.push(view);

    if (data.length > size) {
      data.shift().destroy();
    }
  };

  this.resize = function (newSize, pagesToKeep) {
    size = newSize;

    if (pagesToKeep) {
      const pageIdsToKeep = new Set();

      let i = 0;
      const iMax = pagesToKeep.length;
      for (; i < iMax; ++i) {
        pageIdsToKeep.add(pagesToKeep[i].id);
      }

      (0, _ui_utils.moveToEndOfArray)(data, function (page) {
        return pageIdsToKeep.has(page.id);
      });
    }

    while (data.length > size) {
      data.shift().destroy();
    }
  };
}

function isSameScale(oldScale, newScale) {
  if (newScale === oldScale) {
    return true;
  }

  if (Math.abs(newScale - oldScale) < 1e-15) {
    return true;
  }

  return false;
}

        const BaseViewer =
            /*#__PURE__*/
            function () {
              function BaseViewer(options) {
                const _this = this;

                _classCallCheck(this, BaseViewer);

                if (this.constructor === BaseViewer) {
                  throw new Error('Cannot initialize BaseViewer.');
                }

                this._name = this.constructor.name;
                this.container = options.container;
                this.viewer = options.viewer || options.container.firstElementChild;
                this.eventBus = options.eventBus || (0, _ui_utils.getGlobalEventBus)();
                this.linkService = options.linkService || new _pdf_link_service.SimpleLinkService();
                this.downloadManager = options.downloadManager || null;
                this.findController = options.findController || null;
                this.removePageBorders = options.removePageBorders || false;
                this.textLayerMode = Number.isInteger(options.textLayerMode) ? options.textLayerMode : _ui_utils.TextLayerMode.ENABLE;
                this.imageResourcesPath = options.imageResourcesPath || '';
                this.renderInteractiveForms = options.renderInteractiveForms || false;
                this.enablePrintAutoRotate = options.enablePrintAutoRotate || false;
                this.renderer = options.renderer || _ui_utils.RendererType.CANVAS;
                this.enableWebGL = options.enableWebGL || false;
                this.useOnlyCssZoom = options.useOnlyCssZoom || false;
                this.maxCanvasPixels = options.maxCanvasPixels;
                this.l10n = options.l10n || _ui_utils.NullL10n;
                this.defaultRenderingQueue = !options.renderingQueue;

                if (this.defaultRenderingQueue) {
                  this.renderingQueue = new _pdf_rendering_queue.PDFRenderingQueue();
                  this.renderingQueue.setViewer(this);
                } else {
                  this.renderingQueue = options.renderingQueue;
                }

                this.scroll = (0, _ui_utils.watchScroll)(this.container, this._scrollUpdate.bind(this));
                this.presentationModeState = _ui_utils.PresentationModeState.UNKNOWN;
                this._onBeforeDraw = this._onAfterDraw = null;

                this._resetView();

                if (this.removePageBorders) {
                  this.viewer.classList.add('removePageBorders');
                }

                Promise.resolve().then(function () {
                  _this.eventBus.dispatch('baseviewerinit', {
                    source: _this
                  });
                });
              }

              _createClass(BaseViewer, [{
                key: "getPageView",
                value: function getPageView(index) {
                  return this._pages[index];
                }
              }, {
                key: "_setCurrentPageNumber",
                value: function _setCurrentPageNumber(val) {
                  const resetCurrentPageView = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

                  if (this._currentPageNumber === val) {
                    if (resetCurrentPageView) {
                      this._resetCurrentPageView();
                    }

                    return true;
                  }

                  if (!(0 < val && val <= this.pagesCount)) {
                    return false;
                  }

                  this._currentPageNumber = val;
                  this.eventBus.dispatch('pagechanging', {
                    source: this,
                    pageNumber: val,
                    pageLabel: this._pageLabels && this._pageLabels[val - 1]
                  });

                  if (resetCurrentPageView) {
                    this._resetCurrentPageView();
                  }

                  return true;
                }
              }, {
                key: "setDocument",
                value: function setDocument(pdfDocument) {
                  const _this2 = this;

                  if (this.pdfDocument) {
                    this._cancelRendering();

                    this._resetView();

                    if (this.findController) {
                      this.findController.setDocument(null);
                    }
                  }

                  this.pdfDocument = pdfDocument;

                  if (!pdfDocument) {
                    return;
                  }

                  const pagesCount = pdfDocument.numPages;
                  const pagesCapability = (0, _pdfjsLib.createPromiseCapability)();
                  this.pagesPromise = pagesCapability.promise;
                  pagesCapability.promise.then(function () {
                    _this2._pageViewsReady = true;

                    _this2.eventBus.dispatch('pagesloaded', {
                      source: _this2,
                      pagesCount: pagesCount
                    });
                  });
                  const onePageRenderedCapability = (0, _pdfjsLib.createPromiseCapability)();
                  this.onePageRendered = onePageRenderedCapability.promise;
                  const firstPagePromise = pdfDocument.getPage(1);
                  this.firstPagePromise = firstPagePromise;

                  this._onBeforeDraw = function (evt) {
                    const pageView = _this2._pages[evt.pageNumber - 1];

                    if (!pageView) {
                      return;
                    }

                    _this2._buffer.push(pageView);
                  };

                  this.eventBus.on('pagerender', this._onBeforeDraw);

                  this._onAfterDraw = function (evt) {
                    if (evt.cssTransform || onePageRenderedCapability.settled) {
                      return;
                    }

                    onePageRenderedCapability.resolve();

                    _this2.eventBus.off('pagerendered', _this2._onAfterDraw);

                    _this2._onAfterDraw = null;
                  };

                  this.eventBus.on('pagerendered', this._onAfterDraw);
                  firstPagePromise.then(function (pdfPage) {
                    const scale = _this2.currentScale;
                    const viewport = pdfPage.getViewport({
                      scale: scale * _ui_utils.CSS_UNITS
                    });

                    for (let pageNum = 1; pageNum <= pagesCount; ++pageNum) {
                      let textLayerFactory = null;

                      if (_this2.textLayerMode !== _ui_utils.TextLayerMode.DISABLE) {
                        textLayerFactory = _this2;
                      }

                      const pageView = new _pdf_page_view.PDFPageView({
                        container: _this2._setDocumentViewerElement,
                        eventBus: _this2.eventBus,
                        id: pageNum,
                        scale: scale,
                        defaultViewport: viewport.clone(),
                        renderingQueue: _this2.renderingQueue,
                        textLayerFactory: textLayerFactory,
                        textLayerMode: _this2.textLayerMode,
                        annotationLayerFactory: _this2,
                        imageResourcesPath: _this2.imageResourcesPath,
                        renderInteractiveForms: _this2.renderInteractiveForms,
                        renderer: _this2.renderer,
                        enableWebGL: _this2.enableWebGL,
                        useOnlyCssZoom: _this2.useOnlyCssZoom,
                        maxCanvasPixels: _this2.maxCanvasPixels,
                        l10n: _this2.l10n
                      });

                      _this2._pages.push(pageView);
                    }

                    if (_this2._spreadMode !== _ui_utils.SpreadMode.NONE) {
                      _this2._updateSpreadMode();
                    }

                    onePageRenderedCapability.promise.then(function () {
                      if (_this2.findController) {
                        _this2.findController.setDocument(pdfDocument);
                      }

                      if (pdfDocument.loadingParams['disableAutoFetch']) {
                        pagesCapability.resolve();
                        return;
                      }

                      let getPagesLeft = pagesCount;

                      const _loop = function _loop(_pageNum) {
                        pdfDocument.getPage(_pageNum).then(function (pdfPage) {
                          const pageView = _this2._pages[_pageNum - 1];

                          if (!pageView.pdfPage) {
                            pageView.setPdfPage(pdfPage);
                          }

                          _this2.linkService.cachePageRef(_pageNum, pdfPage.ref);

                          if (--getPagesLeft === 0) {
                            pagesCapability.resolve();
                          }
                        }, function (reason) {
                          console.error("Unable to get page ".concat(_pageNum, " to initialize viewer"), reason);

                          if (--getPagesLeft === 0) {
                            pagesCapability.resolve();
                          }
                        });
                      };

                      for (var _pageNum = 1; _pageNum <= pagesCount; ++_pageNum) {
                        _loop(_pageNum);
                      }
                    });

                    _this2.eventBus.dispatch('pagesinit', {
                      source: _this2
                    });

                    if (_this2.defaultRenderingQueue) {
                      _this2.update();
                    }
                  })["catch"](function (reason) {
                    console.error('Unable to initialize viewer', reason);
                  });
                }
              }, {
                key: "setPageLabels",
                value: function setPageLabels(labels) {
                  if (!this.pdfDocument) {
                    return;
                  }

                  if (!labels) {
                    this._pageLabels = null;
                  } else if (!(Array.isArray(labels) && this.pdfDocument.numPages === labels.length)) {
                    this._pageLabels = null;
                    console.error("".concat(this._name, ".setPageLabels: Invalid page labels."));
                  } else {
                    this._pageLabels = labels;
                  }

                  let i = 0;
                  const ii = this._pages.length;
                  for (; i < ii; i++) {
                    const pageView = this._pages[i];
                    const label = this._pageLabels && this._pageLabels[i];
                    pageView.setPageLabel(label);
                  }
                }
              }, {
                key: "_resetView",
                value: function _resetView() {
                  this._pages = [];
                  this._currentPageNumber = 1;
                  this._currentScale = _ui_utils.UNKNOWN_SCALE;
                  this._currentScaleValue = null;
                  this._pageLabels = null;
                  this._buffer = new PDFPageViewBuffer(DEFAULT_CACHE_SIZE);
                  this._location = null;
                  this._pagesRotation = 0;
                  this._pagesRequests = [];
                  this._pageViewsReady = false;
                  this._scrollMode = _ui_utils.ScrollMode.VERTICAL;
                  this._spreadMode = _ui_utils.SpreadMode.NONE;

                  if (this._onBeforeDraw) {
                    this.eventBus.off('pagerender', this._onBeforeDraw);
                    this._onBeforeDraw = null;
                  }

                  if (this._onAfterDraw) {
                    this.eventBus.off('pagerendered', this._onAfterDraw);
                    this._onAfterDraw = null;
                  }

                  this.viewer.textContent = '';

                  this._updateScrollMode();
                }
              }, {
                key: "_scrollUpdate",
                value: function _scrollUpdate() {
                  if (this.pagesCount === 0) {
                    return;
                  }

                  this.update();
                }
              }, {
                key: "_scrollIntoView",
                value: function _scrollIntoView(_ref) {
                  const pageDiv = _ref.pageDiv,
                      _ref$pageSpot = _ref.pageSpot,
                      pageSpot = _ref$pageSpot === void 0 ? null : _ref$pageSpot,
                      _ref$pageNumber = _ref.pageNumber,
                      pageNumber = _ref$pageNumber === void 0 ? null : _ref$pageNumber;
                  (0, _ui_utils.scrollIntoView)(pageDiv, pageSpot);
                }
              }, {
                key: "_setScaleUpdatePages",
                value: function _setScaleUpdatePages(newScale, newValue) {
                  const noScroll = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
                  const preset = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
                  this._currentScaleValue = newValue.toString();

                  if (isSameScale(this._currentScale, newScale)) {
                    if (preset) {
                      this.eventBus.dispatch('scalechanging', {
                        source: this,
                        scale: newScale,
                        presetValue: newValue
                      });
                    }

                    return;
                  }

                  let i = 0;
                  const ii = this._pages.length;
                  for (; i < ii; i++) {
                    this._pages[i].update(newScale);
                  }

                  this._currentScale = newScale;

                  if (!noScroll) {
                    let page = this._currentPageNumber,
                        dest;

                    if (this._location && !(this.isInPresentationMode || this.isChangingPresentationMode)) {
                      page = this._location.pageNumber;
                      dest = [null, {
                        name: 'XYZ'
                      }, this._location.left, this._location.top, null];
                    }

                    this.scrollPageIntoView({
                      pageNumber: page,
                      destArray: dest,
                      allowNegativeOffset: true
                    });
                  }

                  this.eventBus.dispatch('scalechanging', {
                    source: this,
                    scale: newScale,
                    presetValue: preset ? newValue : undefined
                  });

                  if (this.defaultRenderingQueue) {
                    this.update();
                  }
                }
              }, {
                key: "_setScale",
                value: function _setScale(value) {
                  const noScroll = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  let scale = parseFloat(value);

                  if (scale > 0) {
                    this._setScaleUpdatePages(scale, value, noScroll, false);
                  } else {
                    const currentPage = this._pages[this._currentPageNumber - 1];

                    if (!currentPage) {
                      return;
                    }

                    const noPadding = this.isInPresentationMode || this.removePageBorders;
                    let hPadding = noPadding ? 0 : _ui_utils.SCROLLBAR_PADDING;
                    let vPadding = noPadding ? 0 : _ui_utils.VERTICAL_PADDING;

                    if (!noPadding && this._isScrollModeHorizontal) {
                      const _ref2 = [vPadding, hPadding];
                      hPadding = _ref2[0];
                      vPadding = _ref2[1];
                    }

                    const pageWidthScale = (this.container.clientWidth - hPadding) / currentPage.width * currentPage.scale;
                    const pageHeightScale = (this.container.clientHeight - vPadding) / currentPage.height * currentPage.scale;

                    switch (value) {
                      case 'page-actual':
                        scale = 1;
                        break;

                      case 'page-width':
                        scale = pageWidthScale;
                        break;

                      case 'page-height':
                        scale = pageHeightScale;
                        break;

                      case 'page-fit':
                        scale = Math.min(pageWidthScale, pageHeightScale);
                        break;

                      case 'auto':
                        const horizontalScale = (0, _ui_utils.isPortraitOrientation)(currentPage) ? pageWidthScale : Math.min(pageHeightScale, pageWidthScale);
                        scale = Math.min(_ui_utils.MAX_AUTO_SCALE, horizontalScale);
                        break;

                      default:
                        console.error("".concat(this._name, "._setScale: \"").concat(value, "\" is an unknown zoom value."));
                        return;
                    }

                    this._setScaleUpdatePages(scale, value, noScroll, true);
                  }
                }
              }, {
                key: "_resetCurrentPageView",
                value: function _resetCurrentPageView() {
                  if (this.isInPresentationMode) {
                    this._setScale(this._currentScaleValue, true);
                  }

                  const pageView = this._pages[this._currentPageNumber - 1];

                  this._scrollIntoView({
                    pageDiv: pageView.div
                  });
                }
              }, {
                key: "scrollPageIntoView",
                value: function scrollPageIntoView(_ref3) {
                  const pageNumber = _ref3.pageNumber,
                      _ref3$destArray = _ref3.destArray,
                      destArray = _ref3$destArray === void 0 ? null : _ref3$destArray,
                      _ref3$allowNegativeOf = _ref3.allowNegativeOffset,
                      allowNegativeOffset = _ref3$allowNegativeOf === void 0 ? false : _ref3$allowNegativeOf;

                  if (!this.pdfDocument) {
                    return;
                  }

                  const pageView = Number.isInteger(pageNumber) && this._pages[pageNumber - 1];

                  if (!pageView) {
                    console.error("".concat(this._name, ".scrollPageIntoView: ") + "\"".concat(pageNumber, "\" is not a valid pageNumber parameter."));
                    return;
                  }

                  if (this.isInPresentationMode || !destArray) {
                    this._setCurrentPageNumber(pageNumber, true);

                    return;
                  }

                  let x = 0,
                      y = 0;
                  let width = 0,
                      height = 0,
                      widthScale,
                      heightScale;
                  const changeOrientation = pageView.rotation % 180 === 0 ? false : true;
                  const pageWidth = (changeOrientation ? pageView.height : pageView.width) / pageView.scale / _ui_utils.CSS_UNITS;
                  const pageHeight = (changeOrientation ? pageView.width : pageView.height) / pageView.scale / _ui_utils.CSS_UNITS;
                  let scale = 0;

                  switch (destArray[1].name) {
                    case 'XYZ':
                      x = destArray[2];
                      y = destArray[3];
                      scale = destArray[4];
                      x = x !== null ? x : 0;
                      y = y !== null ? y : pageHeight;
                      break;

                    case 'Fit':
                    case 'FitB':
                      scale = 'page-fit';
                      break;

                    case 'FitH':
                    case 'FitBH':
                      y = destArray[2];
                      scale = 'page-width';

                      if (y === null && this._location) {
                        x = this._location.left;
                        y = this._location.top;
                      }

                      break;

                    case 'FitV':
                    case 'FitBV':
                      x = destArray[2];
                      width = pageWidth;
                      height = pageHeight;
                      scale = 'page-height';
                      break;

                    case 'FitR':
                      x = destArray[2];
                      y = destArray[3];
                      width = destArray[4] - x;
                      height = destArray[5] - y;
                      const hPadding = this.removePageBorders ? 0 : _ui_utils.SCROLLBAR_PADDING;
                      const vPadding = this.removePageBorders ? 0 : _ui_utils.VERTICAL_PADDING;
                      widthScale = (this.container.clientWidth - hPadding) / width / _ui_utils.CSS_UNITS;
                      heightScale = (this.container.clientHeight - vPadding) / height / _ui_utils.CSS_UNITS;
                      scale = Math.min(Math.abs(widthScale), Math.abs(heightScale));
                      break;

                    default:
                      console.error("".concat(this._name, ".scrollPageIntoView: ") + "\"".concat(destArray[1].name, "\" is not a valid destination type."));
                      return;
                  }

                  if (scale && scale !== this._currentScale) {
                    this.currentScaleValue = scale;
                  } else if (this._currentScale === _ui_utils.UNKNOWN_SCALE) {
                    this.currentScaleValue = _ui_utils.DEFAULT_SCALE_VALUE;
                  }

                  if (scale === 'page-fit' && !destArray[4]) {
                    this._scrollIntoView({
                      pageDiv: pageView.div,
                      pageNumber: pageNumber
                    });

                    return;
                  }

                  const boundingRect = [pageView.viewport.convertToViewportPoint(x, y), pageView.viewport.convertToViewportPoint(x + width, y + height)];
                  let left = Math.min(boundingRect[0][0], boundingRect[1][0]);
                  let top = Math.min(boundingRect[0][1], boundingRect[1][1]);

                  if (!allowNegativeOffset) {
                    left = Math.max(left, 0);
                    top = Math.max(top, 0);
                  }

                  this._scrollIntoView({
                    pageDiv: pageView.div,
                    pageSpot: {
                      left: left,
                      top: top
                    },
                    pageNumber: pageNumber
                  });
                }
              }, {
                key: "_updateLocation",
                value: function _updateLocation(firstPage) {
                  const currentScale = this._currentScale;
                  const currentScaleValue = this._currentScaleValue;
                  const normalizedScaleValue = parseFloat(currentScaleValue) === currentScale ? Math.round(currentScale * 10000) / 100 : currentScaleValue;
                  const pageNumber = firstPage.id;
                  let pdfOpenParams = '#page=' + pageNumber;
                  pdfOpenParams += '&zoom=' + normalizedScaleValue;
                  const currentPageView = this._pages[pageNumber - 1];
                  const container = this.container;
                  const topLeft = currentPageView.getPagePoint(container.scrollLeft - firstPage.x, container.scrollTop - firstPage.y);
                  const intLeft = Math.round(topLeft[0]);
                  const intTop = Math.round(topLeft[1]);
                  pdfOpenParams += ',' + intLeft + ',' + intTop;
                  this._location = {
                    pageNumber: pageNumber,
                    scale: normalizedScaleValue,
                    top: intTop,
                    left: intLeft,
                    rotation: this._pagesRotation,
                    pdfOpenParams: pdfOpenParams
                  };
                }
              }, {
                key: "_updateHelper",
                value: function _updateHelper(visiblePages) {
                  throw new Error('Not implemented: _updateHelper');
                }
              }, {
                key: "update",
                value: function update() {
                  const visible = this._getVisiblePages();

                  const visiblePages = visible.views,
                      numVisiblePages = visiblePages.length;

                  if (numVisiblePages === 0) {
                    return;
                  }

                  const newCacheSize = Math.max(DEFAULT_CACHE_SIZE, 2 * numVisiblePages + 1);

                  this._buffer.resize(newCacheSize, visiblePages);

                  this.renderingQueue.renderHighestPriority(visible);

                  this._updateHelper(visiblePages);

                  this._updateLocation(visible.first);

                  this.eventBus.dispatch('updateviewarea', {
                    source: this,
                    location: this._location
                  });
                }
              }, {
                key: "containsElement",
                value: function containsElement(element) {
                  return this.container.contains(element);
                }
              }, {
                key: "focus",
                value: function focus() {
                  this.container.focus();
                }
              }, {
                key: "_getCurrentVisiblePage",
                value: function _getCurrentVisiblePage() {
                  if (!this.pagesCount) {
                    return {
                      views: []
                    };
                  }

                  const pageView = this._pages[this._currentPageNumber - 1];
                  const element = pageView.div;
                  const view = {
                    id: pageView.id,
                    x: element.offsetLeft + element.clientLeft,
                    y: element.offsetTop + element.clientTop,
                    view: pageView
                  };
                  return {
                    first: view,
                    last: view,
                    views: [view]
                  };
                }
              }, {
                key: "_getVisiblePages",
                value: function _getVisiblePages() {
                  return (0, _ui_utils.getVisibleElements)(this.container, this._pages, true, this._isScrollModeHorizontal);
                }
              }, {
                key: "isPageVisible",
                value: function isPageVisible(pageNumber) {
                  if (!this.pdfDocument) {
                    return false;
                  }

                  if (this.pageNumber < 1 || pageNumber > this.pagesCount) {
                    console.error("".concat(this._name, ".isPageVisible: \"").concat(pageNumber, "\" is out of bounds."));
                    return false;
                  }

                  return this._getVisiblePages().views.some(function (view) {
                    return view.id === pageNumber;
                  });
                }
              }, {
                key: "cleanup",
                value: function cleanup() {
                  let i = 0;
                  const ii = this._pages.length;
                  for (; i < ii; i++) {
                    if (this._pages[i] && this._pages[i].renderingState !== _pdf_rendering_queue.RenderingStates.FINISHED) {
                      this._pages[i].reset();
                    }
                  }
                }
              }, {
                key: "_cancelRendering",
                value: function _cancelRendering() {
                  let i = 0;
                  const ii = this._pages.length;
                  for (; i < ii; i++) {
                    if (this._pages[i]) {
                      this._pages[i].cancelRendering();
                    }
                  }
                }
              }, {
                key: "_ensurePdfPageLoaded",
                value: function _ensurePdfPageLoaded(pageView) {
                  const _this3 = this;

                  if (pageView.pdfPage) {
                    return Promise.resolve(pageView.pdfPage);
                  }

                  const pageNumber = pageView.id;

                  if (this._pagesRequests[pageNumber]) {
                    return this._pagesRequests[pageNumber];
                  }

                  const promise = this.pdfDocument.getPage(pageNumber).then(function (pdfPage) {
                    if (!pageView.pdfPage) {
                      pageView.setPdfPage(pdfPage);
                    }

                    _this3._pagesRequests[pageNumber] = null;
                    return pdfPage;
                  })["catch"](function (reason) {
                    console.error('Unable to get page for page view', reason);
                    _this3._pagesRequests[pageNumber] = null;
                  });
                  this._pagesRequests[pageNumber] = promise;
                  return promise;
                }
              }, {
                key: "forceRendering",
                value: function forceRendering(currentlyVisiblePages) {
                  const _this4 = this;

                  const visiblePages = currentlyVisiblePages || this._getVisiblePages();

                  const scrollAhead = this._isScrollModeHorizontal ? this.scroll.right : this.scroll.down;
                  const pageView = this.renderingQueue.getHighestPriority(visiblePages, this._pages, scrollAhead);

                  if (pageView) {
                    this._ensurePdfPageLoaded(pageView).then(function () {
                      _this4.renderingQueue.renderView(pageView);
                    });

                    return true;
                  }

                  return false;
                }
              }, {
                key: "createTextLayerBuilder",
                value: function createTextLayerBuilder(textLayerDiv, pageIndex, viewport) {
                  const enhanceTextSelection = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
                  return new _text_layer_builder.TextLayerBuilder({
                    textLayerDiv: textLayerDiv,
                    eventBus: this.eventBus,
                    pageIndex: pageIndex,
                    viewport: viewport,
                    findController: this.isInPresentationMode ? null : this.findController,
                    enhanceTextSelection: this.isInPresentationMode ? false : enhanceTextSelection
                  });
                }
              }, {
                key: "createAnnotationLayerBuilder",
                value: function createAnnotationLayerBuilder(pageDiv, pdfPage) {
                  const imageResourcesPath = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : '';
                  const renderInteractiveForms = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
                  const l10n = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : _ui_utils.NullL10n;
                  return new _annotation_layer_builder.AnnotationLayerBuilder({
                    pageDiv: pageDiv,
                    pdfPage: pdfPage,
                    imageResourcesPath: imageResourcesPath,
                    renderInteractiveForms: renderInteractiveForms,
                    linkService: this.linkService,
                    downloadManager: this.downloadManager,
                    l10n: l10n
                  });
                }
              }, {
                key: "getPagesOverview",
                value: function getPagesOverview() {
                  const pagesOverview = this._pages.map(function (pageView) {
                    const viewport = pageView.pdfPage.getViewport({
                      scale: 1
                    });
                    return {
                      width: viewport.width,
                      height: viewport.height,
                      rotation: viewport.rotation
                    };
                  });

                  if (!this.enablePrintAutoRotate) {
                    return pagesOverview;
                  }

                  const isFirstPagePortrait = (0, _ui_utils.isPortraitOrientation)(pagesOverview[0]);
                  return pagesOverview.map(function (size) {
                    if (isFirstPagePortrait === (0, _ui_utils.isPortraitOrientation)(size)) {
                      return size;
                    }

                    return {
                      width: size.height,
                      height: size.width,
                      rotation: (size.rotation + 90) % 360
                    };
                  });
                }
              }, {
                key: "_updateScrollMode",
                value: function _updateScrollMode() {
                  const pageNumber = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : null;
                  const scrollMode = this._scrollMode,
                      viewer = this.viewer;
                  viewer.classList.toggle('scrollHorizontal', scrollMode === _ui_utils.ScrollMode.HORIZONTAL);
                  viewer.classList.toggle('scrollWrapped', scrollMode === _ui_utils.ScrollMode.WRAPPED);

                  if (!this.pdfDocument || !pageNumber) {
                    return;
                  }

                  if (this._currentScaleValue && isNaN(this._currentScaleValue)) {
                    this._setScale(this._currentScaleValue, true);
                  }

                  this._setCurrentPageNumber(pageNumber, true);

                  this.update();
                }
              }, {
                key: "_updateSpreadMode",
                value: function _updateSpreadMode() {
                  const pageNumber = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : null;

                  if (!this.pdfDocument) {
                    return;
                  }

                  const viewer = this.viewer,
                      pages = this._pages;
                  viewer.textContent = '';

                  if (this._spreadMode === _ui_utils.SpreadMode.NONE) {
                    let i = 0;
                    const iMax = pages.length;
                    for (; i < iMax; ++i) {
                      viewer.appendChild(pages[i].div);
                    }
                  } else {
                    const parity = this._spreadMode - 1;
                    let spread = null;

                    let _i = 0;
                    const _iMax = pages.length;
                    for (; _i < _iMax; ++_i) {
                      if (spread === null) {
                        spread = document.createElement('div');
                        spread.className = 'spread';
                        viewer.appendChild(spread);
                      } else if (_i % 2 === parity) {
                        spread = spread.cloneNode(false);
                        viewer.appendChild(spread);
                      }

                      spread.appendChild(pages[_i].div);
                    }
                  }

                  if (!pageNumber) {
                    return;
                  }

                  this._setCurrentPageNumber(pageNumber, true);

                  this.update();
                }
              }, {
                key: "pagesCount",
                get: function get() {
                  return this._pages.length;
                }
              }, {
                key: "pageViewsReady",
                get: function get() {
                  return this._pageViewsReady;
                }
              }, {
                key: "currentPageNumber",
                get: function get() {
                  return this._currentPageNumber;
                },
                set: function set(val) {
                  if (!Number.isInteger(val)) {
                    throw new Error('Invalid page number.');
                  }

                  if (!this.pdfDocument) {
                    return;
                  }

                  if (!this._setCurrentPageNumber(val, true)) {
                    console.error("".concat(this._name, ".currentPageNumber: \"").concat(val, "\" is not a valid page."));
                  }
                }
              }, {
                key: "currentPageLabel",
                get: function get() {
                  return this._pageLabels && this._pageLabels[this._currentPageNumber - 1];
                },
                set: function set(val) {
                  if (!this.pdfDocument) {
                    return;
                  }

                  let page = val | 0;

                  if (this._pageLabels) {
                    const i = this._pageLabels.indexOf(val);

                    if (i >= 0) {
                      page = i + 1;
                    }
                  }

                  if (!this._setCurrentPageNumber(page, true)) {
                    console.error("".concat(this._name, ".currentPageLabel: \"").concat(val, "\" is not a valid page."));
                  }
                }
              }, {
                key: "currentScale",
                get: function get() {
                  return this._currentScale !== _ui_utils.UNKNOWN_SCALE ? this._currentScale : _ui_utils.DEFAULT_SCALE;
                },
                set: function set(val) {
                  if (isNaN(val)) {
                    throw new Error('Invalid numeric scale.');
                  }

                  if (!this.pdfDocument) {
                    return;
                  }

                  this._setScale(val, false);
                }
              }, {
                key: "currentScaleValue",
                get: function get() {
                  return this._currentScaleValue;
                },
                set: function set(val) {
                  if (!this.pdfDocument) {
                    return;
                  }

                  this._setScale(val, false);
                }
              }, {
                key: "pagesRotation",
                get: function get() {
                  return this._pagesRotation;
                },
                set: function set(rotation) {
                  if (!(0, _ui_utils.isValidRotation)(rotation)) {
                    throw new Error('Invalid pages rotation angle.');
                  }

                  if (!this.pdfDocument) {
                    return;
                  }

                  if (this._pagesRotation === rotation) {
                    return;
                  }

                  this._pagesRotation = rotation;
                  const pageNumber = this._currentPageNumber;

                  let i = 0;
                  const ii = this._pages.length;
                  for (; i < ii; i++) {
                    const pageView = this._pages[i];
                    pageView.update(pageView.scale, rotation);
                  }

                  if (this._currentScaleValue) {
                    this._setScale(this._currentScaleValue, true);
                  }

                  this.eventBus.dispatch('rotationchanging', {
                    source: this,
                    pagesRotation: rotation,
                    pageNumber: pageNumber
                  });

                  if (this.defaultRenderingQueue) {
                    this.update();
                  }
                }
              }, {
                key: "_setDocumentViewerElement",
                get: function get() {
                  throw new Error('Not implemented: _setDocumentViewerElement');
                }
              }, {
                key: "_isScrollModeHorizontal",
                get: function get() {
                  return this.isInPresentationMode ? false : this._scrollMode === _ui_utils.ScrollMode.HORIZONTAL;
                }
              }, {
                key: "isInPresentationMode",
                get: function get() {
                  return this.presentationModeState === _ui_utils.PresentationModeState.FULLSCREEN;
                }
              }, {
                key: "isChangingPresentationMode",
                get: function get() {
                  return this.presentationModeState === _ui_utils.PresentationModeState.CHANGING;
                }
              }, {
                key: "isHorizontalScrollbarEnabled",
                get: function get() {
                  return this.isInPresentationMode ? false : this.container.scrollWidth > this.container.clientWidth;
                }
              }, {
                key: "isVerticalScrollbarEnabled",
                get: function get() {
                  return this.isInPresentationMode ? false : this.container.scrollHeight > this.container.clientHeight;
                }
              }, {
                key: "hasEqualPageSizes",
                get: function get() {
                  const firstPageView = this._pages[0];

                  let i = 1;
                  const ii = this._pages.length;
                  for (; i < ii; ++i) {
                    const pageView = this._pages[i];

                    if (pageView.width !== firstPageView.width || pageView.height !== firstPageView.height) {
                      return false;
                    }
                  }

                  return true;
                }
              }, {
                key: "scrollMode",
                get: function get() {
                  return this._scrollMode;
                },
                set: function set(mode) {
                  if (this._scrollMode === mode) {
                    return;
                  }

                  if (!(0, _ui_utils.isValidScrollMode)(mode)) {
                    throw new Error("Invalid scroll mode: ".concat(mode));
                  }

                  this._scrollMode = mode;
                  this.eventBus.dispatch('scrollmodechanged', {
                    source: this,
                    mode: mode
                  });

                  this._updateScrollMode(this._currentPageNumber);
                }
              }, {
                key: "spreadMode",
                get: function get() {
                  return this._spreadMode;
                },
                set: function set(mode) {
                  if (this._spreadMode === mode) {
                    return;
                  }

                  if (!(0, _ui_utils.isValidSpreadMode)(mode)) {
                    throw new Error("Invalid spread mode: ".concat(mode));
                  }

                  this._spreadMode = mode;
                  this.eventBus.dispatch('spreadmodechanged', {
                    source: this,
                    mode: mode
                  });

                  this._updateSpreadMode(this._currentPageNumber);
                }
              }]);

              return BaseViewer;
            }();

        exports.BaseViewer = BaseViewer;

/***/ }),
/* 29 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.DefaultAnnotationLayerFactory = exports.AnnotationLayerBuilder = void 0;

        const _pdfjsLib = __webpack_require__(7);

        const _ui_utils = __webpack_require__(5);

        const _pdf_link_service = __webpack_require__(21);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const AnnotationLayerBuilder =
            /*#__PURE__*/
            function () {
              function AnnotationLayerBuilder(_ref) {
                const pageDiv = _ref.pageDiv,
                    pdfPage = _ref.pdfPage,
                    linkService = _ref.linkService,
                    downloadManager = _ref.downloadManager,
                    _ref$imageResourcesPa = _ref.imageResourcesPath,
                    imageResourcesPath = _ref$imageResourcesPa === void 0 ? '' : _ref$imageResourcesPa,
                    _ref$renderInteractiv = _ref.renderInteractiveForms,
                    renderInteractiveForms = _ref$renderInteractiv === void 0 ? false : _ref$renderInteractiv,
                    _ref$l10n = _ref.l10n,
                    l10n = _ref$l10n === void 0 ? _ui_utils.NullL10n : _ref$l10n;

                _classCallCheck(this, AnnotationLayerBuilder);

                this.pageDiv = pageDiv;
                this.pdfPage = pdfPage;
                this.linkService = linkService;
                this.downloadManager = downloadManager;
                this.imageResourcesPath = imageResourcesPath;
                this.renderInteractiveForms = renderInteractiveForms;
                this.l10n = l10n;
                this.div = null;
                this._cancelled = false;
              }

              _createClass(AnnotationLayerBuilder, [{
                key: "render",
                value: function render(viewport) {
                  const _this = this;

                  const intent = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'display';
                  this.pdfPage.getAnnotations({
                    intent: intent
                  }).then(function (annotations) {
                    if (_this._cancelled) {
                      return;
                    }

                    const parameters = {
                      viewport: viewport.clone({
                        dontFlip: true
                      }),
                      div: _this.div,
                      annotations: annotations,
                      page: _this.pdfPage,
                      imageResourcesPath: _this.imageResourcesPath,
                      renderInteractiveForms: _this.renderInteractiveForms,
                      linkService: _this.linkService,
                      downloadManager: _this.downloadManager
                    };

                    if (_this.div) {
                      _pdfjsLib.AnnotationLayer.update(parameters);
                    } else {
                      if (annotations.length === 0) {
                        return;
                      }

                      _this.div = document.createElement('div');
                      _this.div.className = 'annotationLayer';

                      _this.pageDiv.appendChild(_this.div);

                      parameters.div = _this.div;

                      _pdfjsLib.AnnotationLayer.render(parameters);

                      _this.l10n.translate(_this.div);
                    }
                  });
                }
              }, {
                key: "cancel",
                value: function cancel() {
                  this._cancelled = true;
                }
              }, {
                key: "hide",
                value: function hide() {
                  if (!this.div) {
                    return;
                  }

                  this.div.setAttribute('hidden', 'true');
                }
              }]);

              return AnnotationLayerBuilder;
            }();

        exports.AnnotationLayerBuilder = AnnotationLayerBuilder;

        const DefaultAnnotationLayerFactory =
            /*#__PURE__*/
            function () {
              function DefaultAnnotationLayerFactory() {
                _classCallCheck(this, DefaultAnnotationLayerFactory);
              }

              _createClass(DefaultAnnotationLayerFactory, [{
                key: "createAnnotationLayerBuilder",
                value: function createAnnotationLayerBuilder(pageDiv, pdfPage) {
                  const imageResourcesPath = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : '';
                  const renderInteractiveForms = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
                  const l10n = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : _ui_utils.NullL10n;
                  return new AnnotationLayerBuilder({
                    pageDiv: pageDiv,
                    pdfPage: pdfPage,
                    imageResourcesPath: imageResourcesPath,
                    renderInteractiveForms: renderInteractiveForms,
                    linkService: new _pdf_link_service.SimpleLinkService(),
                    l10n: l10n
                  });
                }
              }]);

              return DefaultAnnotationLayerFactory;
            }();

        exports.DefaultAnnotationLayerFactory = DefaultAnnotationLayerFactory;

/***/ }),
/* 30 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFPageView = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        const _ui_utils = __webpack_require__(5);

        const _pdfjsLib = __webpack_require__(7);

        const _pdf_rendering_queue = __webpack_require__(11);

        const _viewer_compatibility = __webpack_require__(8);

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const MAX_CANVAS_PIXELS = _viewer_compatibility.viewerCompatibilityParams.maxCanvasPixels || 16777216;

        const PDFPageView =
            /*#__PURE__*/
            function () {
              function PDFPageView(options) {
                _classCallCheck(this, PDFPageView);

                const container = options.container;
                const defaultViewport = options.defaultViewport;
                this.id = options.id;
                this.renderingId = 'page' + this.id;
                this.pdfPage = null;
                this.pageLabel = null;
                this.rotation = 0;
                this.scale = options.scale || _ui_utils.DEFAULT_SCALE;
                this.viewport = defaultViewport;
                this.pdfPageRotate = defaultViewport.rotation;
                this.hasRestrictedScaling = false;
                this.textLayerMode = Number.isInteger(options.textLayerMode) ? options.textLayerMode : _ui_utils.TextLayerMode.ENABLE;
                this.imageResourcesPath = options.imageResourcesPath || '';
                this.renderInteractiveForms = options.renderInteractiveForms || false;
                this.useOnlyCssZoom = options.useOnlyCssZoom || false;
                this.maxCanvasPixels = options.maxCanvasPixels || MAX_CANVAS_PIXELS;
                this.eventBus = options.eventBus || (0, _ui_utils.getGlobalEventBus)();
                this.renderingQueue = options.renderingQueue;
                this.textLayerFactory = options.textLayerFactory;
                this.annotationLayerFactory = options.annotationLayerFactory;
                this.renderer = options.renderer || _ui_utils.RendererType.CANVAS;
                this.enableWebGL = options.enableWebGL || false;
                this.l10n = options.l10n || _ui_utils.NullL10n;
                this.paintTask = null;
                this.paintedViewportMap = new WeakMap();
                this.renderingState = _pdf_rendering_queue.RenderingStates.INITIAL;
                this.resume = null;
                this.error = null;
                this.annotationLayer = null;
                this.textLayer = null;
                this.zoomLayer = null;
                const div = document.createElement('div');
                div.className = 'page';
                div.style.width = Math.floor(this.viewport.width) + 'px';
                div.style.height = Math.floor(this.viewport.height) + 'px';
                div.setAttribute('data-page-number', this.id);
                this.div = div;
                container.appendChild(div);
              }

              _createClass(PDFPageView, [{
                key: "setPdfPage",
                value: function setPdfPage(pdfPage) {
                  this.pdfPage = pdfPage;
                  this.pdfPageRotate = pdfPage.rotate;
                  const totalRotation = (this.rotation + this.pdfPageRotate) % 360;
                  this.viewport = pdfPage.getViewport({
                    scale: this.scale * _ui_utils.CSS_UNITS,
                    rotation: totalRotation
                  });
                  this.stats = pdfPage.stats;
                  this.reset();
                }
              }, {
                key: "destroy",
                value: function destroy() {
                  this.reset();

                  if (this.pdfPage) {
                    this.pdfPage.cleanup();
                  }
                }
              }, {
                key: "_resetZoomLayer",
                value: function _resetZoomLayer() {
                  const removeFromDOM = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

                  if (!this.zoomLayer) {
                    return;
                  }

                  const zoomLayerCanvas = this.zoomLayer.firstChild;
                  this.paintedViewportMap["delete"](zoomLayerCanvas);
                  zoomLayerCanvas.width = 0;
                  zoomLayerCanvas.height = 0;

                  if (removeFromDOM) {
                    this.zoomLayer.remove();
                  }

                  this.zoomLayer = null;
                }
              }, {
                key: "reset",
                value: function reset() {
                  const keepZoomLayer = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
                  const keepAnnotations = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  this.cancelRendering(keepAnnotations);
                  this.renderingState = _pdf_rendering_queue.RenderingStates.INITIAL;
                  const div = this.div;
                  div.style.width = Math.floor(this.viewport.width) + 'px';
                  div.style.height = Math.floor(this.viewport.height) + 'px';
                  const childNodes = div.childNodes;
                  const currentZoomLayerNode = keepZoomLayer && this.zoomLayer || null;
                  const currentAnnotationNode = keepAnnotations && this.annotationLayer && this.annotationLayer.div || null;

                  for (let i = childNodes.length - 1; i >= 0; i--) {
                    const node = childNodes[i];

                    if (currentZoomLayerNode === node || currentAnnotationNode === node) {
                      continue;
                    }

                    div.removeChild(node);
                  }

                  div.removeAttribute('data-loaded');

                  if (currentAnnotationNode) {
                    this.annotationLayer.hide();
                  } else if (this.annotationLayer) {
                    this.annotationLayer.cancel();
                    this.annotationLayer = null;
                  }

                  if (!currentZoomLayerNode) {
                    if (this.canvas) {
                      this.paintedViewportMap["delete"](this.canvas);
                      this.canvas.width = 0;
                      this.canvas.height = 0;
                      delete this.canvas;
                    }

                    this._resetZoomLayer();
                  }

                  if (this.svg) {
                    this.paintedViewportMap["delete"](this.svg);
                    delete this.svg;
                  }

                  this.loadingIconDiv = document.createElement('div');
                  this.loadingIconDiv.className = 'loadingIcon';
                  div.appendChild(this.loadingIconDiv);
                }
              }, {
                key: "update",
                value: function update(scale, rotation) {
                  this.scale = scale || this.scale;

                  if (typeof rotation !== 'undefined') {
                    this.rotation = rotation;
                  }

                  const totalRotation = (this.rotation + this.pdfPageRotate) % 360;
                  this.viewport = this.viewport.clone({
                    scale: this.scale * _ui_utils.CSS_UNITS,
                    rotation: totalRotation
                  });

                  if (this.svg) {
                    this.cssTransform(this.svg, true);
                    this.eventBus.dispatch('pagerendered', {
                      source: this,
                      pageNumber: this.id,
                      cssTransform: true,
                      timestamp: performance.now()
                    });
                    return;
                  }

                  let isScalingRestricted = false;

                  if (this.canvas && this.maxCanvasPixels > 0) {
                    const outputScale = this.outputScale;

                    if ((Math.floor(this.viewport.width) * outputScale.sx | 0) * (Math.floor(this.viewport.height) * outputScale.sy | 0) > this.maxCanvasPixels) {
                      isScalingRestricted = true;
                    }
                  }

                  if (this.canvas) {
                    if (this.useOnlyCssZoom || this.hasRestrictedScaling && isScalingRestricted) {
                      this.cssTransform(this.canvas, true);
                      this.eventBus.dispatch('pagerendered', {
                        source: this,
                        pageNumber: this.id,
                        cssTransform: true,
                        timestamp: performance.now()
                      });
                      return;
                    }

                    if (!this.zoomLayer && !this.canvas.hasAttribute('hidden')) {
                      this.zoomLayer = this.canvas.parentNode;
                      this.zoomLayer.style.position = 'absolute';
                    }
                  }

                  if (this.zoomLayer) {
                    this.cssTransform(this.zoomLayer.firstChild);
                  }

                  this.reset(true, true);
                }
              }, {
                key: "cancelRendering",
                value: function cancelRendering() {
                  const keepAnnotations = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

                  if (this.paintTask) {
                    this.paintTask.cancel();
                    this.paintTask = null;
                  }

                  this.resume = null;

                  if (this.textLayer) {
                    this.textLayer.cancel();
                    this.textLayer = null;
                  }

                  if (!keepAnnotations && this.annotationLayer) {
                    this.annotationLayer.cancel();
                    this.annotationLayer = null;
                  }
                }
              }, {
                key: "cssTransform",
                value: function cssTransform(target) {
                  const redrawAnnotations = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
                  const width = this.viewport.width;
                  const height = this.viewport.height;
                  const div = this.div;
                  target.style.width = target.parentNode.style.width = div.style.width = Math.floor(width) + 'px';
                  target.style.height = target.parentNode.style.height = div.style.height = Math.floor(height) + 'px';
                  const relativeRotation = this.viewport.rotation - this.paintedViewportMap.get(target).rotation;
                  const absRotation = Math.abs(relativeRotation);
                  let scaleX = 1,
                      scaleY = 1;

                  if (absRotation === 90 || absRotation === 270) {
                    scaleX = height / width;
                    scaleY = width / height;
                  }

                  target.style.transform = 'rotate(' + relativeRotation + 'deg) ' + 'scale(' + scaleX + ',' + scaleY + ')';

                  if (this.textLayer) {
                    const textLayerViewport = this.textLayer.viewport;
                    const textRelativeRotation = this.viewport.rotation - textLayerViewport.rotation;
                    const textAbsRotation = Math.abs(textRelativeRotation);
                    let scale = width / textLayerViewport.width;

                    if (textAbsRotation === 90 || textAbsRotation === 270) {
                      scale = width / textLayerViewport.height;
                    }

                    const textLayerDiv = this.textLayer.textLayerDiv;
                    let transX, transY;

                    switch (textAbsRotation) {
                      case 0:
                        transX = transY = 0;
                        break;

                      case 90:
                        transX = 0;
                        transY = '-' + textLayerDiv.style.height;
                        break;

                      case 180:
                        transX = '-' + textLayerDiv.style.width;
                        transY = '-' + textLayerDiv.style.height;
                        break;

                      case 270:
                        transX = '-' + textLayerDiv.style.width;
                        transY = 0;
                        break;

                      default:
                        console.error('Bad rotation value.');
                        break;
                    }

                    textLayerDiv.style.transform = 'rotate(' + textAbsRotation + 'deg) ' + 'scale(' + scale + ', ' + scale + ') ' + 'translate(' + transX + ', ' + transY + ')';
                    textLayerDiv.style.transformOrigin = '0% 0%';
                  }

                  if (redrawAnnotations && this.annotationLayer) {
                    this.annotationLayer.render(this.viewport, 'display');
                  }
                }
              }, {
                key: "getPagePoint",
                value: function getPagePoint(x, y) {
                  return this.viewport.convertToPdfPoint(x, y);
                }
              }, {
                key: "draw",
                value: function draw() {
                  const _this = this;

                  if (this.renderingState !== _pdf_rendering_queue.RenderingStates.INITIAL) {
                    console.error('Must be in new state before drawing');
                    this.reset();
                  }

                  if (!this.pdfPage) {
                    this.renderingState = _pdf_rendering_queue.RenderingStates.FINISHED;
                    return Promise.reject(new Error('Page is not loaded'));
                  }

                  this.renderingState = _pdf_rendering_queue.RenderingStates.RUNNING;
                  const pdfPage = this.pdfPage;
                  const div = this.div;
                  const canvasWrapper = document.createElement('div');
                  canvasWrapper.style.width = div.style.width;
                  canvasWrapper.style.height = div.style.height;
                  canvasWrapper.classList.add('canvasWrapper');

                  if (this.annotationLayer && this.annotationLayer.div) {
                    div.insertBefore(canvasWrapper, this.annotationLayer.div);
                  } else {
                    div.appendChild(canvasWrapper);
                  }

                  let textLayer = null;

                  if (this.textLayerMode !== _ui_utils.TextLayerMode.DISABLE && this.textLayerFactory) {
                    const textLayerDiv = document.createElement('div');
                    textLayerDiv.className = 'textLayer';
                    textLayerDiv.style.width = canvasWrapper.style.width;
                    textLayerDiv.style.height = canvasWrapper.style.height;

                    if (this.annotationLayer && this.annotationLayer.div) {
                      div.insertBefore(textLayerDiv, this.annotationLayer.div);
                    } else {
                      div.appendChild(textLayerDiv);
                    }

                    textLayer = this.textLayerFactory.createTextLayerBuilder(textLayerDiv, this.id - 1, this.viewport, this.textLayerMode === _ui_utils.TextLayerMode.ENABLE_ENHANCE);
                  }

                  this.textLayer = textLayer;
                  let renderContinueCallback = null;

                  if (this.renderingQueue) {
                    renderContinueCallback = function renderContinueCallback(cont) {
                      if (!_this.renderingQueue.isHighestPriority(_this)) {
                        _this.renderingState = _pdf_rendering_queue.RenderingStates.PAUSED;

                        _this.resume = function () {
                          _this.renderingState = _pdf_rendering_queue.RenderingStates.RUNNING;
                          cont();
                        };

                        return;
                      }

                      cont();
                    };
                  }

                  const finishPaintTask =
                      /*#__PURE__*/
                      function () {
                        const _ref = _asyncToGenerator(
                            /*#__PURE__*/
                            _regenerator["default"].mark(function _callee(error) {
                              return _regenerator["default"].wrap(function _callee$(_context) {
                                while (1) {
                                  switch (_context.prev = _context.next) {
                                    case 0:
                                      if (paintTask === _this.paintTask) {
                                        _this.paintTask = null;
                                      }

                                      if (!(error instanceof _pdfjsLib.RenderingCancelledException)) {
                                        _context.next = 4;
                                        break;
                                      }

                                      _this.error = null;
                                      return _context.abrupt("return");

                                    case 4:
                                      _this.renderingState = _pdf_rendering_queue.RenderingStates.FINISHED;

                                      if (_this.loadingIconDiv) {
                                        div.removeChild(_this.loadingIconDiv);
                                        delete _this.loadingIconDiv;
                                      }

                                      _this._resetZoomLayer(true);

                                      _this.error = error;
                                      _this.stats = pdfPage.stats;

                                      _this.eventBus.dispatch('pagerendered', {
                                        source: _this,
                                        pageNumber: _this.id,
                                        cssTransform: false,
                                        timestamp: performance.now()
                                      });

                                      if (!error) {
                                        _context.next = 12;
                                        break;
                                      }

                                      throw error;

                                    case 12:
                                    case "end":
                                      return _context.stop();
                                  }
                                }
                              }, _callee);
                            }));

                        return function finishPaintTask(_x) {
                          return _ref.apply(this, arguments);
                        };
                      }();

                  var paintTask = this.renderer === _ui_utils.RendererType.SVG ? this.paintOnSvg(canvasWrapper) : this.paintOnCanvas(canvasWrapper);
                  paintTask.onRenderContinue = renderContinueCallback;
                  this.paintTask = paintTask;
                  const resultPromise = paintTask.promise.then(function () {
                    return finishPaintTask(null).then(function () {
                      if (textLayer) {
                        const readableStream = pdfPage.streamTextContent({
                          normalizeWhitespace: true
                        });
                        textLayer.setTextContentStream(readableStream);
                        textLayer.render();
                      }
                    });
                  }, function (reason) {
                    return finishPaintTask(reason);
                  });

                  if (this.annotationLayerFactory) {
                    if (!this.annotationLayer) {
                      this.annotationLayer = this.annotationLayerFactory.createAnnotationLayerBuilder(div, pdfPage, this.imageResourcesPath, this.renderInteractiveForms, this.l10n);
                    }

                    this.annotationLayer.render(this.viewport, 'display');
                  }

                  div.setAttribute('data-loaded', true);
                  this.eventBus.dispatch('pagerender', {
                    source: this,
                    pageNumber: this.id
                  });
                  return resultPromise;
                }
              }, {
                key: "paintOnCanvas",
                value: function paintOnCanvas(canvasWrapper) {
                  const renderCapability = (0, _pdfjsLib.createPromiseCapability)();
                  const result = {
                    promise: renderCapability.promise,
                    onRenderContinue: function onRenderContinue(cont) {
                      cont();
                    },
                    cancel: function cancel() {
                      renderTask.cancel();
                    }
                  };
                  const viewport = this.viewport;
                  const canvas = document.createElement('canvas');
                  canvas.id = this.renderingId;
                  canvas.setAttribute('hidden', 'hidden');
                  let isCanvasHidden = true;

                  const showCanvas = function showCanvas() {
                    if (isCanvasHidden) {
                      canvas.removeAttribute('hidden');
                      isCanvasHidden = false;
                    }
                  };

                  canvasWrapper.appendChild(canvas);
                  this.canvas = canvas;
                  canvas.mozOpaque = true;
                  const ctx = canvas.getContext('2d', {
                    alpha: false
                  });
                  const outputScale = (0, _ui_utils.getOutputScale)(ctx);
                  this.outputScale = outputScale;

                  if (this.useOnlyCssZoom) {
                    const actualSizeViewport = viewport.clone({
                      scale: _ui_utils.CSS_UNITS
                    });
                    outputScale.sx *= actualSizeViewport.width / viewport.width;
                    outputScale.sy *= actualSizeViewport.height / viewport.height;
                    outputScale.scaled = true;
                  }

                  if (this.maxCanvasPixels > 0) {
                    const pixelsInViewport = viewport.width * viewport.height;
                    const maxScale = Math.sqrt(this.maxCanvasPixels / pixelsInViewport);

                    if (outputScale.sx > maxScale || outputScale.sy > maxScale) {
                      outputScale.sx = maxScale;
                      outputScale.sy = maxScale;
                      outputScale.scaled = true;
                      this.hasRestrictedScaling = true;
                    } else {
                      this.hasRestrictedScaling = false;
                    }
                  }

                  const sfx = (0, _ui_utils.approximateFraction)(outputScale.sx);
                  const sfy = (0, _ui_utils.approximateFraction)(outputScale.sy);
                  canvas.width = (0, _ui_utils.roundToDivide)(viewport.width * outputScale.sx, sfx[0]);
                  canvas.height = (0, _ui_utils.roundToDivide)(viewport.height * outputScale.sy, sfy[0]);
                  canvas.style.width = (0, _ui_utils.roundToDivide)(viewport.width, sfx[1]) + 'px';
                  canvas.style.height = (0, _ui_utils.roundToDivide)(viewport.height, sfy[1]) + 'px';
                  this.paintedViewportMap.set(canvas, viewport);
                  const transform = !outputScale.scaled ? null : [outputScale.sx, 0, 0, outputScale.sy, 0, 0];
                  const renderContext = {
                    canvasContext: ctx,
                    transform: transform,
                    viewport: this.viewport,
                    enableWebGL: this.enableWebGL,
                    renderInteractiveForms: this.renderInteractiveForms
                  };
                  var renderTask = this.pdfPage.render(renderContext);

                  renderTask.onContinue = function (cont) {
                    showCanvas();

                    if (result.onRenderContinue) {
                      result.onRenderContinue(cont);
                    } else {
                      cont();
                    }
                  };

                  renderTask.promise.then(function () {
                    showCanvas();
                    renderCapability.resolve(undefined);
                  }, function (error) {
                    showCanvas();
                    renderCapability.reject(error);
                  });
                  return result;
                }
              }, {
                key: "paintOnSvg",
                value: function paintOnSvg(wrapper) {
                  const _this2 = this;

                  let cancelled = false;

                  const ensureNotCancelled = function ensureNotCancelled() {
                    if (cancelled) {
                      throw new _pdfjsLib.RenderingCancelledException('Rendering cancelled, page ' + _this2.id, 'svg');
                    }
                  };

                  const pdfPage = this.pdfPage;
                  const actualSizeViewport = this.viewport.clone({
                    scale: _ui_utils.CSS_UNITS
                  });
                  const promise = pdfPage.getOperatorList().then(function (opList) {
                    ensureNotCancelled();
                    const svgGfx = new _pdfjsLib.SVGGraphics(pdfPage.commonObjs, pdfPage.objs);
                    return svgGfx.getSVG(opList, actualSizeViewport).then(function (svg) {
                      ensureNotCancelled();
                      _this2.svg = svg;

                      _this2.paintedViewportMap.set(svg, actualSizeViewport);

                      svg.style.width = wrapper.style.width;
                      svg.style.height = wrapper.style.height;
                      _this2.renderingState = _pdf_rendering_queue.RenderingStates.FINISHED;
                      wrapper.appendChild(svg);
                    });
                  });
                  return {
                    promise: promise,
                    onRenderContinue: function onRenderContinue(cont) {
                      cont();
                    },
                    cancel: function cancel() {
                      cancelled = true;
                    }
                  };
                }
              }, {
                key: "setPageLabel",
                value: function setPageLabel(label) {
                  this.pageLabel = typeof label === 'string' ? label : null;

                  if (this.pageLabel !== null) {
                    this.div.setAttribute('data-page-label', this.pageLabel);
                  } else {
                    this.div.removeAttribute('data-page-label');
                  }
                }
              }, {
                key: "width",
                get: function get() {
                  return this.viewport.width;
                }
              }, {
                key: "height",
                get: function get() {
                  return this.viewport.height;
                }
              }]);

              return PDFPageView;
            }();

        exports.PDFPageView = PDFPageView;

/***/ }),
/* 31 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.DefaultTextLayerFactory = exports.TextLayerBuilder = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdfjsLib = __webpack_require__(7);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const EXPAND_DIVS_TIMEOUT = 300;

        const TextLayerBuilder =
            /*#__PURE__*/
            function () {
              function TextLayerBuilder(_ref) {
                const textLayerDiv = _ref.textLayerDiv,
                    eventBus = _ref.eventBus,
                    pageIndex = _ref.pageIndex,
                    viewport = _ref.viewport,
                    _ref$findController = _ref.findController,
                    findController = _ref$findController === void 0 ? null : _ref$findController,
                    _ref$enhanceTextSelec = _ref.enhanceTextSelection,
                    enhanceTextSelection = _ref$enhanceTextSelec === void 0 ? false : _ref$enhanceTextSelec;

                _classCallCheck(this, TextLayerBuilder);

                this.textLayerDiv = textLayerDiv;
                this.eventBus = eventBus || (0, _ui_utils.getGlobalEventBus)();
                this.textContent = null;
                this.textContentItemsStr = [];
                this.textContentStream = null;
                this.renderingDone = false;
                this.pageIdx = pageIndex;
                this.pageNumber = this.pageIdx + 1;
                this.matches = [];
                this.viewport = viewport;
                this.textDivs = [];
                this.findController = findController;
                this.textLayerRenderTask = null;
                this.enhanceTextSelection = enhanceTextSelection;
                this._onUpdateTextLayerMatches = null;

                this._bindMouse();
              }

              _createClass(TextLayerBuilder, [{
                key: "_finishRendering",
                value: function _finishRendering() {
                  this.renderingDone = true;

                  if (!this.enhanceTextSelection) {
                    const endOfContent = document.createElement('div');
                    endOfContent.className = 'endOfContent';
                    this.textLayerDiv.appendChild(endOfContent);
                  }

                  this.eventBus.dispatch('textlayerrendered', {
                    source: this,
                    pageNumber: this.pageNumber,
                    numTextDivs: this.textDivs.length
                  });
                }
              }, {
                key: "render",
                value: function render() {
                  const _this = this;

                  const timeout = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;

                  if (!(this.textContent || this.textContentStream) || this.renderingDone) {
                    return;
                  }

                  this.cancel();
                  this.textDivs = [];
                  const textLayerFrag = document.createDocumentFragment();
                  this.textLayerRenderTask = (0, _pdfjsLib.renderTextLayer)({
                    textContent: this.textContent,
                    textContentStream: this.textContentStream,
                    container: textLayerFrag,
                    viewport: this.viewport,
                    textDivs: this.textDivs,
                    textContentItemsStr: this.textContentItemsStr,
                    timeout: timeout,
                    enhanceTextSelection: this.enhanceTextSelection
                  });
                  this.textLayerRenderTask.promise.then(function () {
                    _this.textLayerDiv.appendChild(textLayerFrag);

                    _this._finishRendering();

                    _this._updateMatches();
                  }, function (reason) {
                  });

                  if (!this._onUpdateTextLayerMatches) {
                    this._onUpdateTextLayerMatches = function (evt) {
                      if (evt.pageIndex === _this.pageIdx || evt.pageIndex === -1) {
                        _this._updateMatches();
                      }
                    };

                    this.eventBus.on('updatetextlayermatches', this._onUpdateTextLayerMatches);
                  }
                }
              }, {
                key: "cancel",
                value: function cancel() {
                  if (this.textLayerRenderTask) {
                    this.textLayerRenderTask.cancel();
                    this.textLayerRenderTask = null;
                  }

                  if (this._onUpdateTextLayerMatches) {
                    this.eventBus.off('updatetextlayermatches', this._onUpdateTextLayerMatches);
                    this._onUpdateTextLayerMatches = null;
                  }
                }
              }, {
                key: "setTextContentStream",
                value: function setTextContentStream(readableStream) {
                  this.cancel();
                  this.textContentStream = readableStream;
                }
              }, {
                key: "setTextContent",
                value: function setTextContent(textContent) {
                  this.cancel();
                  this.textContent = textContent;
                }
              }, {
                key: "_convertMatches",
                value: function _convertMatches(matches, matchesLength) {
                  if (!matches) {
                    return [];
                  }

                  const findController = this.findController,
                      textContentItemsStr = this.textContentItemsStr;
                  let i = 0,
                      iIndex = 0;
                  const end = textContentItemsStr.length - 1;
                  const queryLen = findController.state.query.length;
                  const result = [];

                  let m = 0;
                  const mm = matches.length;
                  for (; m < mm; m++) {
                    let matchIdx = matches[m];

                    while (i !== end && matchIdx >= iIndex + textContentItemsStr[i].length) {
                      iIndex += textContentItemsStr[i].length;
                      i++;
                    }

                    if (i === textContentItemsStr.length) {
                      console.error('Could not find a matching mapping');
                    }

                    const match = {
                      begin: {
                        divIdx: i,
                        offset: matchIdx - iIndex
                      }
                    };

                    if (matchesLength) {
                      matchIdx += matchesLength[m];
                    } else {
                      matchIdx += queryLen;
                    }

                    while (i !== end && matchIdx > iIndex + textContentItemsStr[i].length) {
                      iIndex += textContentItemsStr[i].length;
                      i++;
                    }

                    match.end = {
                      divIdx: i,
                      offset: matchIdx - iIndex
                    };
                    result.push(match);
                  }

                  return result;
                }
              }, {
                key: "_renderMatches",
                value: function _renderMatches(matches) {
                  if (matches.length === 0) {
                    return;
                  }

                  const findController = this.findController,
                      pageIdx = this.pageIdx,
                      textContentItemsStr = this.textContentItemsStr,
                      textDivs = this.textDivs;
                  const isSelectedPage = pageIdx === findController.selected.pageIdx;
                  const selectedMatchIdx = findController.selected.matchIdx;
                  const highlightAll = findController.state.highlightAll;
                  let prevEnd = null;
                  const infinity = {
                    divIdx: -1,
                    offset: undefined
                  };

                  function beginText(begin, className) {
                    const divIdx = begin.divIdx;
                    textDivs[divIdx].textContent = '';
                    appendTextToDiv(divIdx, 0, begin.offset, className);
                  }

                  function appendTextToDiv(divIdx, fromOffset, toOffset, className) {
                    const div = textDivs[divIdx];
                    const content = textContentItemsStr[divIdx].substring(fromOffset, toOffset);
                    const node = document.createTextNode(content);

                    if (className) {
                      const span = document.createElement('span');
                      span.className = className;
                      span.appendChild(node);
                      div.appendChild(span);
                      return;
                    }

                    div.appendChild(node);
                  }

                  let i0 = selectedMatchIdx,
                      i1 = i0 + 1;

                  if (highlightAll) {
                    i0 = 0;
                    i1 = matches.length;
                  } else if (!isSelectedPage) {
                    return;
                  }

                  for (let i = i0; i < i1; i++) {
                    const match = matches[i];
                    var begin = match.begin;
                    const end = match.end;
                    const isSelected = isSelectedPage && i === selectedMatchIdx;
                    const highlightSuffix = isSelected ? ' selected' : '';

                    if (isSelected) {
                      findController.scrollMatchIntoView({
                        element: textDivs[begin.divIdx],
                        pageIndex: pageIdx,
                        matchIndex: selectedMatchIdx
                      });
                    }

                    if (!prevEnd || begin.divIdx !== prevEnd.divIdx) {
                      if (prevEnd !== null) {
                        appendTextToDiv(prevEnd.divIdx, prevEnd.offset, infinity.offset);
                      }

                      beginText(begin);
                    } else {
                      appendTextToDiv(prevEnd.divIdx, prevEnd.offset, begin.offset);
                    }

                    if (begin.divIdx === end.divIdx) {
                      appendTextToDiv(begin.divIdx, begin.offset, end.offset, 'highlight' + highlightSuffix);
                    } else {
                      appendTextToDiv(begin.divIdx, begin.offset, infinity.offset, 'highlight begin' + highlightSuffix);

                      let n0 = begin.divIdx + 1;
                      const n1 = end.divIdx;
                      for (; n0 < n1; n0++) {
                        textDivs[n0].className = 'highlight middle' + highlightSuffix;
                      }

                      beginText(end, 'highlight end' + highlightSuffix);
                    }

                    prevEnd = end;
                  }

                  if (prevEnd) {
                    appendTextToDiv(prevEnd.divIdx, prevEnd.offset, infinity.offset);
                  }
                }
              }, {
                key: "_updateMatches",
                value: function _updateMatches() {
                  if (!this.renderingDone) {
                    return;
                  }

                  const findController = this.findController,
                      matches = this.matches,
                      pageIdx = this.pageIdx,
                      textContentItemsStr = this.textContentItemsStr,
                      textDivs = this.textDivs;
                  let clearedUntilDivIdx = -1;

                  let i = 0;
                  const ii = matches.length;
                  for (; i < ii; i++) {
                    const match = matches[i];
                    const begin = Math.max(clearedUntilDivIdx, match.begin.divIdx);

                    let n = begin;
                    const end = match.end.divIdx;
                    for (; n <= end; n++) {
                      const div = textDivs[n];
                      div.textContent = textContentItemsStr[n];
                      div.className = '';
                    }

                    clearedUntilDivIdx = match.end.divIdx + 1;
                  }

                  if (!findController || !findController.highlightMatches) {
                    return;
                  }

                  const pageMatches = findController.pageMatches[pageIdx] || null;
                  const pageMatchesLength = findController.pageMatchesLength[pageIdx] || null;
                  this.matches = this._convertMatches(pageMatches, pageMatchesLength);

                  this._renderMatches(this.matches);
                }
              }, {
                key: "_bindMouse",
                value: function _bindMouse() {
                  const _this2 = this;

                  const div = this.textLayerDiv;
                  let expandDivsTimer = null;
                  div.addEventListener('mousedown', function (evt) {
                    if (_this2.enhanceTextSelection && _this2.textLayerRenderTask) {
                      _this2.textLayerRenderTask.expandTextDivs(true);

                      if (expandDivsTimer) {
                        clearTimeout(expandDivsTimer);
                        expandDivsTimer = null;
                      }

                      return;
                    }

                    const end = div.querySelector('.endOfContent');

                    if (!end) {
                      return;
                    }

                    let adjustTop = evt.target !== div;
                    adjustTop = adjustTop && window.getComputedStyle(end).getPropertyValue('-moz-user-select') !== 'none';

                    if (adjustTop) {
                      const divBounds = div.getBoundingClientRect();
                      const r = Math.max(0, (evt.pageY - divBounds.top) / divBounds.height);
                      end.style.top = (r * 100).toFixed(2) + '%';
                    }

                    end.classList.add('active');
                  });
                  div.addEventListener('mouseup', function () {
                    if (_this2.enhanceTextSelection && _this2.textLayerRenderTask) {
                      expandDivsTimer = setTimeout(function () {
                        if (_this2.textLayerRenderTask) {
                          _this2.textLayerRenderTask.expandTextDivs(false);
                        }

                        expandDivsTimer = null;
                      }, EXPAND_DIVS_TIMEOUT);
                      return;
                    }

                    const end = div.querySelector('.endOfContent');

                    if (!end) {
                      return;
                    }

                    end.style.top = '';
                    end.classList.remove('active');
                  });
                }
              }]);

              return TextLayerBuilder;
            }();

        exports.TextLayerBuilder = TextLayerBuilder;

        const DefaultTextLayerFactory =
            /*#__PURE__*/
            function () {
              function DefaultTextLayerFactory() {
                _classCallCheck(this, DefaultTextLayerFactory);
              }

              _createClass(DefaultTextLayerFactory, [{
                key: "createTextLayerBuilder",
                value: function createTextLayerBuilder(textLayerDiv, pageIndex, viewport) {
                  const enhanceTextSelection = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : false;
                  return new TextLayerBuilder({
                    textLayerDiv: textLayerDiv,
                    pageIndex: pageIndex,
                    viewport: viewport,
                    enhanceTextSelection: enhanceTextSelection
                  });
                }
              }]);

              return DefaultTextLayerFactory;
            }();

        exports.DefaultTextLayerFactory = DefaultTextLayerFactory;

/***/ }),
/* 32 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SecondaryToolbar = void 0;

        const _ui_utils = __webpack_require__(5);

        const _pdf_cursor_tools = __webpack_require__(9);

        const _pdf_single_page_viewer = __webpack_require__(33);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const SecondaryToolbar =
            /*#__PURE__*/
            function () {
              function SecondaryToolbar(options, mainContainer, eventBus) {
                const _this = this;

                _classCallCheck(this, SecondaryToolbar);

                this.toolbar = options.toolbar;
                this.toggleButton = options.toggleButton;
                this.toolbarButtonContainer = options.toolbarButtonContainer;
                this.buttons = [{
                  element: options.presentationModeButton,
                  eventName: 'presentationmode',
                  close: true
                }, {
                  element: options.openFileButton,
                  eventName: 'openfile',
                  close: true
                }, {
                  element: options.printButton,
                  eventName: 'print',
                  close: true
                }, {
                  element: options.downloadButton,
                  eventName: 'download',
                  close: true
                }, {
                  element: options.viewBookmarkButton,
                  eventName: null,
                  close: true
                }, {
                  element: options.firstPageButton,
                  eventName: 'firstpage',
                  close: true
                }, {
                  element: options.lastPageButton,
                  eventName: 'lastpage',
                  close: true
                }, {
                  element: options.pageRotateCwButton,
                  eventName: 'rotatecw',
                  close: false
                }, {
                  element: options.pageRotateCcwButton,
                  eventName: 'rotateccw',
                  close: false
                }, {
                  element: options.cursorSelectToolButton,
                  eventName: 'switchcursortool',
                  eventDetails: {
                    tool: _pdf_cursor_tools.CursorTool.SELECT
                  },
                  close: true
                }, {
                  element: options.cursorHandToolButton,
                  eventName: 'switchcursortool',
                  eventDetails: {
                    tool: _pdf_cursor_tools.CursorTool.HAND
                  },
                  close: true
                }, {
                  element: options.scrollVerticalButton,
                  eventName: 'switchscrollmode',
                  eventDetails: {
                    mode: _ui_utils.ScrollMode.VERTICAL
                  },
                  close: true
                }, {
                  element: options.scrollHorizontalButton,
                  eventName: 'switchscrollmode',
                  eventDetails: {
                    mode: _ui_utils.ScrollMode.HORIZONTAL
                  },
                  close: true
                }, {
                  element: options.scrollWrappedButton,
                  eventName: 'switchscrollmode',
                  eventDetails: {
                    mode: _ui_utils.ScrollMode.WRAPPED
                  },
                  close: true
                }, {
                  element: options.spreadNoneButton,
                  eventName: 'switchspreadmode',
                  eventDetails: {
                    mode: _ui_utils.SpreadMode.NONE
                  },
                  close: true
                }, {
                  element: options.spreadOddButton,
                  eventName: 'switchspreadmode',
                  eventDetails: {
                    mode: _ui_utils.SpreadMode.ODD
                  },
                  close: true
                }, {
                  element: options.spreadEvenButton,
                  eventName: 'switchspreadmode',
                  eventDetails: {
                    mode: _ui_utils.SpreadMode.EVEN
                  },
                  close: true
                }, {
                  element: options.documentPropertiesButton,
                  eventName: 'documentproperties',
                  close: true
                }];
                this.items = {
                  firstPage: options.firstPageButton,
                  lastPage: options.lastPageButton,
                  pageRotateCw: options.pageRotateCwButton,
                  pageRotateCcw: options.pageRotateCcwButton
                };
                this.mainContainer = mainContainer;
                this.eventBus = eventBus;
                this.opened = false;
                this.containerHeight = null;
                this.previousContainerHeight = null;
                this.reset();

                this._bindClickListeners();

                this._bindCursorToolsListener(options);

                this._bindScrollModeListener(options);

                this._bindSpreadModeListener(options);

                this.eventBus.on('resize', this._setMaxHeight.bind(this));
                this.eventBus.on('baseviewerinit', function (evt) {
                  if (evt.source instanceof _pdf_single_page_viewer.PDFSinglePageViewer) {
                    _this.toolbarButtonContainer.classList.add('hiddenScrollModeButtons', 'hiddenSpreadModeButtons');
                  } else {
                    _this.toolbarButtonContainer.classList.remove('hiddenScrollModeButtons', 'hiddenSpreadModeButtons');
                  }
                });
              }

              _createClass(SecondaryToolbar, [{
                key: "setPageNumber",
                value: function setPageNumber(pageNumber) {
                  this.pageNumber = pageNumber;

                  this._updateUIState();
                }
              }, {
                key: "setPagesCount",
                value: function setPagesCount(pagesCount) {
                  this.pagesCount = pagesCount;

                  this._updateUIState();
                }
              }, {
                key: "reset",
                value: function reset() {
                  this.pageNumber = 0;
                  this.pagesCount = 0;

                  this._updateUIState();

                  this.eventBus.dispatch('secondarytoolbarreset', {
                    source: this
                  });
                }
              }, {
                key: "_updateUIState",
                value: function _updateUIState() {
                  this.items.firstPage.disabled = this.pageNumber <= 1;
                  this.items.lastPage.disabled = this.pageNumber >= this.pagesCount;
                  this.items.pageRotateCw.disabled = this.pagesCount === 0;
                  this.items.pageRotateCcw.disabled = this.pagesCount === 0;
                }
              }, {
                key: "_bindClickListeners",
                value: function _bindClickListeners() {
                  const _this2 = this;

                  this.toggleButton.addEventListener('click', this.toggle.bind(this));

                  const _loop = function _loop(button) {
                    const _this2$buttons$button = _this2.buttons[button],
                        element = _this2$buttons$button.element,
                        eventName = _this2$buttons$button.eventName,
                        close = _this2$buttons$button.close,
                        eventDetails = _this2$buttons$button.eventDetails;
                    element.addEventListener('click', function (evt) {
                      if (eventName !== null) {
                        const details = {
                          source: _this2
                        };

                        for (let property in eventDetails) {
                          details[property] = eventDetails[property];
                        }

                        _this2.eventBus.dispatch(eventName, details);
                      }

                      if (close) {
                        _this2.close();
                      }
                    });
                  };

                  for (var button in this.buttons) {
                    _loop(button);
                  }
                }
              }, {
                key: "_bindCursorToolsListener",
                value: function _bindCursorToolsListener(buttons) {
                  this.eventBus.on('cursortoolchanged', function (_ref) {
                    const tool = _ref.tool;
                    buttons.cursorSelectToolButton.classList.toggle('toggled', tool === _pdf_cursor_tools.CursorTool.SELECT);
                    buttons.cursorHandToolButton.classList.toggle('toggled', tool === _pdf_cursor_tools.CursorTool.HAND);
                  });
                }
              }, {
                key: "_bindScrollModeListener",
                value: function _bindScrollModeListener(buttons) {
                  const _this3 = this;

                  function scrollModeChanged(_ref2) {
                    const mode = _ref2.mode;
                    buttons.scrollVerticalButton.classList.toggle('toggled', mode === _ui_utils.ScrollMode.VERTICAL);
                    buttons.scrollHorizontalButton.classList.toggle('toggled', mode === _ui_utils.ScrollMode.HORIZONTAL);
                    buttons.scrollWrappedButton.classList.toggle('toggled', mode === _ui_utils.ScrollMode.WRAPPED);
                    const isScrollModeHorizontal = mode === _ui_utils.ScrollMode.HORIZONTAL;
                    buttons.spreadNoneButton.disabled = isScrollModeHorizontal;
                    buttons.spreadOddButton.disabled = isScrollModeHorizontal;
                    buttons.spreadEvenButton.disabled = isScrollModeHorizontal;
                  }

                  this.eventBus.on('scrollmodechanged', scrollModeChanged);
                  this.eventBus.on('secondarytoolbarreset', function (evt) {
                    if (evt.source === _this3) {
                      scrollModeChanged({
                        mode: _ui_utils.ScrollMode.VERTICAL
                      });
                    }
                  });
                }
              }, {
                key: "_bindSpreadModeListener",
                value: function _bindSpreadModeListener(buttons) {
                  const _this4 = this;

                  function spreadModeChanged(_ref3) {
                    const mode = _ref3.mode;
                    buttons.spreadNoneButton.classList.toggle('toggled', mode === _ui_utils.SpreadMode.NONE);
                    buttons.spreadOddButton.classList.toggle('toggled', mode === _ui_utils.SpreadMode.ODD);
                    buttons.spreadEvenButton.classList.toggle('toggled', mode === _ui_utils.SpreadMode.EVEN);
                  }

                  this.eventBus.on('spreadmodechanged', spreadModeChanged);
                  this.eventBus.on('secondarytoolbarreset', function (evt) {
                    if (evt.source === _this4) {
                      spreadModeChanged({
                        mode: _ui_utils.SpreadMode.NONE
                      });
                    }
                  });
                }
              }, {
                key: "open",
                value: function open() {
                  if (this.opened) {
                    return;
                  }

                  this.opened = true;

                  this._setMaxHeight();

                  this.toggleButton.classList.add('toggled');
                  this.toolbar.classList.remove('hidden');
                }
              }, {
                key: "close",
                value: function close() {
                  if (!this.opened) {
                    return;
                  }

                  this.opened = false;
                  this.toolbar.classList.add('hidden');
                  this.toggleButton.classList.remove('toggled');
                }
              }, {
                key: "toggle",
                value: function toggle() {
                  if (this.opened) {
                    this.close();
                  } else {
                    this.open();
                  }
                }
              }, {
                key: "_setMaxHeight",
                value: function _setMaxHeight() {
                  if (!this.opened) {
                    return;
                  }

                  this.containerHeight = this.mainContainer.clientHeight;

                  if (this.containerHeight === this.previousContainerHeight) {
                    return;
                  }

                  this.toolbarButtonContainer.setAttribute('style', 'max-height: ' + (this.containerHeight - _ui_utils.SCROLLBAR_PADDING) + 'px;');
                  this.previousContainerHeight = this.containerHeight;
                }
              }, {
                key: "isOpen",
                get: function get() {
                  return this.opened;
                }
              }]);

              return SecondaryToolbar;
            }();

        exports.SecondaryToolbar = SecondaryToolbar;

/***/ }),
/* 33 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFSinglePageViewer = void 0;

        const _base_viewer = __webpack_require__(28);

        const _pdfjsLib = __webpack_require__(7);

        function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _get(target, property, receiver) { if (typeof Reflect !== "undefined" && Reflect.get) { _get = Reflect.get; } else { _get = function _get(target, property, receiver) {
  const base = _superPropBase(target, property);
  if (!base) return;
  const desc = Object.getOwnPropertyDescriptor(base, property);
  if (desc.get) { return desc.get.call(receiver); } return desc.value; }; } return _get(target, property, receiver || target); }

function _superPropBase(object, property) { while (!Object.prototype.hasOwnProperty.call(object, property)) { object = _getPrototypeOf(object); if (object === null) break; } return object; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

        const PDFSinglePageViewer =
            /*#__PURE__*/
            function (_BaseViewer) {
              _inherits(PDFSinglePageViewer, _BaseViewer);

              function PDFSinglePageViewer(options) {
                let _this;

                _classCallCheck(this, PDFSinglePageViewer);

                _this = _possibleConstructorReturn(this, _getPrototypeOf(PDFSinglePageViewer).call(this, options));

                _this.eventBus.on('pagesinit', function (evt) {
                  _this._ensurePageViewVisible();
                });

                return _this;
              }

              _createClass(PDFSinglePageViewer, [{
                key: "_resetView",
                value: function _resetView() {
                  _get(_getPrototypeOf(PDFSinglePageViewer.prototype), "_resetView", this).call(this);

                  this._previousPageNumber = 1;
                  this._shadowViewer = document.createDocumentFragment();
                  this._updateScrollDown = null;
                }
              }, {
                key: "_ensurePageViewVisible",
                value: function _ensurePageViewVisible() {
                  const pageView = this._pages[this._currentPageNumber - 1];
                  const previousPageView = this._pages[this._previousPageNumber - 1];
                  const viewerNodes = this.viewer.childNodes;

                  switch (viewerNodes.length) {
                    case 0:
                      this.viewer.appendChild(pageView.div);
                      break;

                    case 1:
                      if (viewerNodes[0] !== previousPageView.div) {
                        throw new Error('_ensurePageViewVisible: Unexpected previously visible page.');
                      }

                      if (pageView === previousPageView) {
                        break;
                      }

                      this._shadowViewer.appendChild(previousPageView.div);

                      this.viewer.appendChild(pageView.div);
                      this.container.scrollTop = 0;
                      break;

                    default:
                      throw new Error('_ensurePageViewVisible: Only one page should be visible at a time.');
                  }

                  this._previousPageNumber = this._currentPageNumber;
                }
              }, {
                key: "_scrollUpdate",
                value: function _scrollUpdate() {
                  if (this._updateScrollDown) {
                    this._updateScrollDown();
                  }

                  _get(_getPrototypeOf(PDFSinglePageViewer.prototype), "_scrollUpdate", this).call(this);
                }
              }, {
                key: "_scrollIntoView",
                value: function _scrollIntoView(_ref) {
                  const _this2 = this;

                  const pageDiv = _ref.pageDiv,
                      _ref$pageSpot = _ref.pageSpot,
                      pageSpot = _ref$pageSpot === void 0 ? null : _ref$pageSpot,
                      _ref$pageNumber = _ref.pageNumber,
                      pageNumber = _ref$pageNumber === void 0 ? null : _ref$pageNumber;

                  if (pageNumber) {
                    this._setCurrentPageNumber(pageNumber);
                  }

                  const scrolledDown = this._currentPageNumber >= this._previousPageNumber;

                  this._ensurePageViewVisible();

                  this.update();

                  _get(_getPrototypeOf(PDFSinglePageViewer.prototype), "_scrollIntoView", this).call(this, {
                    pageDiv: pageDiv,
                    pageSpot: pageSpot,
                    pageNumber: pageNumber
                  });

                  this._updateScrollDown = function () {
                    _this2.scroll.down = scrolledDown;
                    _this2._updateScrollDown = null;
                  };
                }
              }, {
                key: "_getVisiblePages",
                value: function _getVisiblePages() {
                  return this._getCurrentVisiblePage();
                }
              }, {
                key: "_updateHelper",
                value: function _updateHelper(visiblePages) {
                }
              }, {
                key: "_updateScrollMode",
                value: function _updateScrollMode() {
                }
              }, {
                key: "_updateSpreadMode",
                value: function _updateSpreadMode() {
                }
              }, {
                key: "_setDocumentViewerElement",
                get: function get() {
                  return (0, _pdfjsLib.shadow)(this, '_setDocumentViewerElement', this._shadowViewer);
                }
              }, {
                key: "_isScrollModeHorizontal",
                get: function get() {
                  return (0, _pdfjsLib.shadow)(this, '_isScrollModeHorizontal', false);
                }
              }]);

              return PDFSinglePageViewer;
            }(_base_viewer.BaseViewer);

        exports.PDFSinglePageViewer = PDFSinglePageViewer;

/***/ }),
/* 34 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Toolbar = void 0;

        const _ui_utils = __webpack_require__(5);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const PAGE_NUMBER_LOADING_INDICATOR = 'visiblePageIsLoading';
        const SCALE_SELECT_CONTAINER_PADDING = 8;
        const SCALE_SELECT_PADDING = 22;

        const Toolbar =
            /*#__PURE__*/
            function () {
              function Toolbar(options, eventBus) {
                const l10n = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : _ui_utils.NullL10n;

                _classCallCheck(this, Toolbar);

                this.toolbar = options.container;
                this.eventBus = eventBus;
                this.l10n = l10n;
                this.items = options;
                this._wasLocalized = false;
                this.reset();

                this._bindListeners();
              }

              _createClass(Toolbar, [{
                key: "setPageNumber",
                value: function setPageNumber(pageNumber, pageLabel) {
                  this.pageNumber = pageNumber;
                  this.pageLabel = pageLabel;

                  this._updateUIState(false);
                }
              }, {
                key: "setPagesCount",
                value: function setPagesCount(pagesCount, hasPageLabels) {
                  this.pagesCount = pagesCount;
                  this.hasPageLabels = hasPageLabels;

                  this._updateUIState(true);
                }
              }, {
                key: "setPageScale",
                value: function setPageScale(pageScaleValue, pageScale) {
                  this.pageScaleValue = (pageScaleValue || pageScale).toString();
                  this.pageScale = pageScale;

                  this._updateUIState(false);
                }
              }, {
                key: "reset",
                value: function reset() {
                  this.pageNumber = 0;
                  this.pageLabel = null;
                  this.hasPageLabels = false;
                  this.pagesCount = 0;
                  this.pageScaleValue = _ui_utils.DEFAULT_SCALE_VALUE;
                  this.pageScale = _ui_utils.DEFAULT_SCALE;

                  this._updateUIState(true);

                  this.updateLoadingIndicatorState();
                }
              }, {
                key: "_bindListeners",
                value: function _bindListeners() {
                  const _this = this;

                  const eventBus = this.eventBus,
                      items = this.items;
                  const self = this;
                  items.previous.addEventListener('click', function () {
                    eventBus.dispatch('previouspage', {
                      source: self
                    });
                  });
                  items.next.addEventListener('click', function () {
                    eventBus.dispatch('nextpage', {
                      source: self
                    });
                  });
                  items.zoomIn.addEventListener('click', function () {
                    eventBus.dispatch('zoomin', {
                      source: self
                    });
                  });
                  items.zoomOut.addEventListener('click', function () {
                    eventBus.dispatch('zoomout', {
                      source: self
                    });
                  });
                  items.pageNumber.addEventListener('click', function () {
                    this.select();
                  });
                  items.pageNumber.addEventListener('change', function () {
                    eventBus.dispatch('pagenumberchanged', {
                      source: self,
                      value: this.value
                    });
                  });
                  items.scaleSelect.addEventListener('change', function () {
                    if (this.value === 'custom') {
                      return;
                    }

                    eventBus.dispatch('scalechanged', {
                      source: self,
                      value: this.value
                    });
                  });
                  items.presentationModeButton.addEventListener('click', function () {
                    eventBus.dispatch('presentationmode', {
                      source: self
                    });
                  });
                  items.openFile.addEventListener('click', function () {
                    eventBus.dispatch('openfile', {
                      source: self
                    });
                  });
                  items.print.addEventListener('click', function () {
                    eventBus.dispatch('print', {
                      source: self
                    });
                  });
                  items.download.addEventListener('click', function () {
                    eventBus.dispatch('download', {
                      source: self
                    });
                  });
                  items.scaleSelect.oncontextmenu = _ui_utils.noContextMenuHandler;
                  eventBus.on('localized', function () {
                    _this._localized();
                  });
                }
              }, {
                key: "_localized",
                value: function _localized() {
                  this._wasLocalized = true;

                  this._adjustScaleWidth();

                  this._updateUIState(true);
                }
              }, {
                key: "_updateUIState",
                value: function _updateUIState() {
                  const resetNumPages = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;

                  if (!this._wasLocalized) {
                    return;
                  }

                  const pageNumber = this.pageNumber,
                      pagesCount = this.pagesCount,
                      pageScaleValue = this.pageScaleValue,
                      pageScale = this.pageScale,
                      items = this.items;

                  if (resetNumPages) {
                    if (this.hasPageLabels) {
                      items.pageNumber.type = 'text';
                    } else {
                      items.pageNumber.type = 'number';
                      this.l10n.get('of_pages', {
                        pagesCount: pagesCount
                      }, 'of {{pagesCount}}').then(function (msg) {
                        items.numPages.textContent = msg;
                      });
                    }

                    items.pageNumber.max = pagesCount;
                  }

                  if (this.hasPageLabels) {
                    items.pageNumber.value = this.pageLabel;
                    this.l10n.get('page_of_pages', {
                      pageNumber: pageNumber,
                      pagesCount: pagesCount
                    }, '({{pageNumber}} of {{pagesCount}})').then(function (msg) {
                      items.numPages.textContent = msg;
                    });
                  } else {
                    items.pageNumber.value = pageNumber;
                  }

                  items.previous.disabled = pageNumber <= 1;
                  items.next.disabled = pageNumber >= pagesCount;
                  items.zoomOut.disabled = pageScale <= _ui_utils.MIN_SCALE;
                  items.zoomIn.disabled = pageScale >= _ui_utils.MAX_SCALE;
                  const customScale = Math.round(pageScale * 10000) / 100;
                  this.l10n.get('page_scale_percent', {
                    scale: customScale
                  }, '{{scale}}%').then(function (msg) {
                    const options = items.scaleSelect.options;
                    let predefinedValueFound = false;

                    let i = 0;
                    const ii = options.length;
                    for (; i < ii; i++) {
                      const option = options[i];

                      if (option.value !== pageScaleValue) {
                        option.selected = false;
                        continue;
                      }

                      option.selected = true;
                      predefinedValueFound = true;
                    }

                    if (!predefinedValueFound) {
                      items.customScaleOption.textContent = msg;
                      items.customScaleOption.selected = true;
                    }
                  });
                }
              }, {
                key: "updateLoadingIndicatorState",
                value: function updateLoadingIndicatorState() {
                  const loading = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
                  const pageNumberInput = this.items.pageNumber;
                  pageNumberInput.classList.toggle(PAGE_NUMBER_LOADING_INDICATOR, loading);
                }
              }, {
                key: "_adjustScaleWidth",
                value: function _adjustScaleWidth() {
                  const container = this.items.scaleSelectContainer;
                  const select = this.items.scaleSelect;

                  _ui_utils.animationStarted.then(function () {
                    if (container.clientWidth === 0) {
                      container.setAttribute('style', 'display: inherit;');
                    }

                    if (container.clientWidth > 0) {
                      select.setAttribute('style', 'min-width: inherit;');
                      const width = select.clientWidth + SCALE_SELECT_CONTAINER_PADDING;
                      select.setAttribute('style', 'min-width: ' + (width + SCALE_SELECT_PADDING) + 'px;');
                      container.setAttribute('style', 'min-width: ' + width + 'px; ' + 'max-width: ' + width + 'px;');
                    }
                  });
                }
              }]);

              return Toolbar;
            }();

        exports.Toolbar = Toolbar;

/***/ }),
/* 35 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ViewHistory = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const DEFAULT_VIEW_HISTORY_CACHE_SIZE = 20;

        const ViewHistory =
            /*#__PURE__*/
            function () {
              function ViewHistory(fingerprint) {
                const _this = this;

                const cacheSize = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : DEFAULT_VIEW_HISTORY_CACHE_SIZE;

                _classCallCheck(this, ViewHistory);

                this.fingerprint = fingerprint;
                this.cacheSize = cacheSize;
                this._initializedPromise = this._readFromStorage().then(function (databaseStr) {
                  const database = JSON.parse(databaseStr || '{}');

                  if (!('files' in database)) {
                    database.files = [];
                  } else {
                    while (database.files.length >= _this.cacheSize) {
                      database.files.shift();
                    }
                  }

                  let index = -1;

                  let i = 0;
                  const length = database.files.length;
                  for (; i < length; i++) {
                    const branch = database.files[i];

                    if (branch.fingerprint === _this.fingerprint) {
                      index = i;
                      break;
                    }
                  }

                  if (index === -1) {
                    index = database.files.push({
                      fingerprint: _this.fingerprint
                    }) - 1;
                  }

                  _this.file = database.files[index];
                  _this.database = database;
                });
              }

              _createClass(ViewHistory, [{
                key: "_writeToStorage",
                value: function () {
                  const _writeToStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee() {
                        let databaseStr;
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                databaseStr = JSON.stringify(this.database);
                                localStorage.setItem('pdfjs.history', databaseStr);

                              case 2:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee, this);
                      }));

                  function _writeToStorage() {
                    return _writeToStorage2.apply(this, arguments);
                  }

                  return _writeToStorage;
                }()
              }, {
                key: "_readFromStorage",
                value: function () {
                  const _readFromStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2() {
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                return _context2.abrupt("return", localStorage.getItem('pdfjs.history'));

                              case 1:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2);
                      }));

                  function _readFromStorage() {
                    return _readFromStorage2.apply(this, arguments);
                  }

                  return _readFromStorage;
                }()
              }, {
                key: "set",
                value: function () {
                  const _set = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee3(name, val) {
                        return _regenerator["default"].wrap(function _callee3$(_context3) {
                          while (1) {
                            switch (_context3.prev = _context3.next) {
                              case 0:
                                _context3.next = 2;
                                return this._initializedPromise;

                              case 2:
                                this.file[name] = val;
                                return _context3.abrupt("return", this._writeToStorage());

                              case 4:
                              case "end":
                                return _context3.stop();
                            }
                          }
                        }, _callee3, this);
                      }));

                  function set(_x, _x2) {
                    return _set.apply(this, arguments);
                  }

                  return set;
                }()
              }, {
                key: "setMultiple",
                value: function () {
                  const _setMultiple = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee4(properties) {
                        let name;
                        return _regenerator["default"].wrap(function _callee4$(_context4) {
                          while (1) {
                            switch (_context4.prev = _context4.next) {
                              case 0:
                                _context4.next = 2;
                                return this._initializedPromise;

                              case 2:
                                for (name in properties) {
                                  this.file[name] = properties[name];
                                }

                                return _context4.abrupt("return", this._writeToStorage());

                              case 4:
                              case "end":
                                return _context4.stop();
                            }
                          }
                        }, _callee4, this);
                      }));

                  function setMultiple(_x3) {
                    return _setMultiple.apply(this, arguments);
                  }

                  return setMultiple;
                }()
              }, {
                key: "get",
                value: function () {
                  const _get = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee5(name, defaultValue) {
                        let val;
                        return _regenerator["default"].wrap(function _callee5$(_context5) {
                          while (1) {
                            switch (_context5.prev = _context5.next) {
                              case 0:
                                _context5.next = 2;
                                return this._initializedPromise;

                              case 2:
                                val = this.file[name];
                                return _context5.abrupt("return", val !== undefined ? val : defaultValue);

                              case 4:
                              case "end":
                                return _context5.stop();
                            }
                          }
                        }, _callee5, this);
                      }));

                  function get(_x4, _x5) {
                    return _get.apply(this, arguments);
                  }

                  return get;
                }()
              }, {
                key: "getMultiple",
                value: function () {
                  const _getMultiple = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee6(properties) {
                        let values, name, val;
                        return _regenerator["default"].wrap(function _callee6$(_context6) {
                          while (1) {
                            switch (_context6.prev = _context6.next) {
                              case 0:
                                _context6.next = 2;
                                return this._initializedPromise;

                              case 2:
                                values = Object.create(null);

                                for (name in properties) {
                                  val = this.file[name];
                                  values[name] = val !== undefined ? val : properties[name];
                                }

                                return _context6.abrupt("return", values);

                              case 5:
                              case "end":
                                return _context6.stop();
                            }
                          }
                        }, _callee6, this);
                      }));

                  function getMultiple(_x6) {
                    return _getMultiple.apply(this, arguments);
                  }

                  return getMultiple;
                }()
              }]);

              return ViewHistory;
            }();

        exports.ViewHistory = ViewHistory;

/***/ }),
/* 36 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.GenericCom = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        const _app = __webpack_require__(1);

        const _preferences = __webpack_require__(37);

        const _download_manager = __webpack_require__(38);

        const _genericl10n = __webpack_require__(39);

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

;
        const GenericCom = {};
        exports.GenericCom = GenericCom;

        const GenericPreferences =
            /*#__PURE__*/
            function (_BasePreferences) {
              _inherits(GenericPreferences, _BasePreferences);

              function GenericPreferences() {
                _classCallCheck(this, GenericPreferences);

                return _possibleConstructorReturn(this, _getPrototypeOf(GenericPreferences).apply(this, arguments));
              }

              _createClass(GenericPreferences, [{
                key: "_writeToStorage",
                value: function () {
                  const _writeToStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee(prefObj) {
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                localStorage.setItem('pdfjs.preferences', JSON.stringify(prefObj));

                              case 1:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee);
                      }));

                  function _writeToStorage(_x) {
                    return _writeToStorage2.apply(this, arguments);
                  }

                  return _writeToStorage;
                }()
              }, {
                key: "_readFromStorage",
                value: function () {
                  const _readFromStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2(prefObj) {
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                return _context2.abrupt("return", JSON.parse(localStorage.getItem('pdfjs.preferences')));

                              case 1:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2);
                      }));

                  function _readFromStorage(_x2) {
                    return _readFromStorage2.apply(this, arguments);
                  }

                  return _readFromStorage;
                }()
              }]);

              return GenericPreferences;
            }(_preferences.BasePreferences);

        const GenericExternalServices = Object.create(_app.DefaultExternalServices);

        GenericExternalServices.createDownloadManager = function (options) {
  return new _download_manager.DownloadManager(options);
};

GenericExternalServices.createPreferences = function () {
  return new GenericPreferences();
};

GenericExternalServices.createL10n = function (_ref) {
  const _ref$locale = _ref.locale,
      locale = _ref$locale === void 0 ? 'en-US' : _ref$locale;
  return new _genericl10n.GenericL10n(locale);
};

_app.PDFViewerApplication.externalServices = GenericExternalServices;

/***/ }),
/* 37 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.BasePreferences = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        let defaultPreferences = null;

        function getDefaultPreferences() {
  if (!defaultPreferences) {
    defaultPreferences = Promise.resolve({
      "cursorToolOnLoad": 0,
      "defaultZoomValue": "",
      "disablePageLabels": false,
      "enablePrintAutoRotate": false,
      "enableWebGL": false,
      "eventBusDispatchToDOM": false,
      "externalLinkTarget": 0,
      "historyUpdateUrl": false,
      "pdfBugEnabled": false,
      "renderer": "canvas",
      "renderInteractiveForms": false,
      "sidebarViewOnLoad": -1,
      "scrollModeOnLoad": -1,
      "spreadModeOnLoad": -1,
      "textLayerMode": 1,
      "useOnlyCssZoom": false,
      "viewOnLoad": 0,
      "disableAutoFetch": false,
      "disableFontFace": false,
      "disableRange": false,
      "disableStream": false
    });
  }

  return defaultPreferences;
}

        const BasePreferences =
            /*#__PURE__*/
            function () {
              function BasePreferences() {
                const _this = this;

                _classCallCheck(this, BasePreferences);

                if (this.constructor === BasePreferences) {
                  throw new Error('Cannot initialize BasePreferences.');
                }

                this.prefs = null;
                this._initializedPromise = getDefaultPreferences().then(function (defaults) {
                  Object.defineProperty(_this, 'defaults', {
                    value: Object.freeze(defaults),
                    writable: false,
                    enumerable: true,
                    configurable: false
                  });
                  _this.prefs = Object.assign(Object.create(null), defaults);
                  return _this._readFromStorage(defaults);
                }).then(function (prefs) {
                  if (!prefs) {
                    return;
                  }

                  for (let name in prefs) {
                    const defaultValue = _this.defaults[name],
                        prefValue = prefs[name];

                    if (defaultValue === undefined || _typeof(prefValue) !== _typeof(defaultValue)) {
                      continue;
                    }

                    _this.prefs[name] = prefValue;
                  }
                });
              }

              _createClass(BasePreferences, [{
                key: "_writeToStorage",
                value: function () {
                  const _writeToStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee(prefObj) {
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                throw new Error('Not implemented: _writeToStorage');

                              case 1:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee);
                      }));

                  function _writeToStorage(_x) {
                    return _writeToStorage2.apply(this, arguments);
                  }

                  return _writeToStorage;
                }()
              }, {
                key: "_readFromStorage",
                value: function () {
                  const _readFromStorage2 = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2(prefObj) {
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                throw new Error('Not implemented: _readFromStorage');

                              case 1:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2);
                      }));

                  function _readFromStorage(_x2) {
                    return _readFromStorage2.apply(this, arguments);
                  }

                  return _readFromStorage;
                }()
              }, {
                key: "reset",
                value: function () {
                  const _reset = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee3() {
                        return _regenerator["default"].wrap(function _callee3$(_context3) {
                          while (1) {
                            switch (_context3.prev = _context3.next) {
                              case 0:
                                _context3.next = 2;
                                return this._initializedPromise;

                              case 2:
                                this.prefs = Object.assign(Object.create(null), this.defaults);
                                return _context3.abrupt("return", this._writeToStorage(this.defaults));

                              case 4:
                              case "end":
                                return _context3.stop();
                            }
                          }
                        }, _callee3, this);
                      }));

                  function reset() {
                    return _reset.apply(this, arguments);
                  }

                  return reset;
                }()
              }, {
                key: "set",
                value: function () {
                  const _set = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee4(name, value) {
                        let defaultValue, valueType, defaultType;
                        return _regenerator["default"].wrap(function _callee4$(_context4) {
                          while (1) {
                            switch (_context4.prev = _context4.next) {
                              case 0:
                                _context4.next = 2;
                                return this._initializedPromise;

                              case 2:
                                defaultValue = this.defaults[name];

                                if (!(defaultValue === undefined)) {
                                  _context4.next = 7;
                                  break;
                                }

                                throw new Error("Set preference: \"".concat(name, "\" is undefined."));

                              case 7:
                                if (!(value === undefined)) {
                                  _context4.next = 9;
                                  break;
                                }

                                throw new Error('Set preference: no value is specified.');

                              case 9:
                                valueType = _typeof(value);
                                defaultType = _typeof(defaultValue);

                                if (!(valueType !== defaultType)) {
                                  _context4.next = 19;
                                  break;
                                }

                                if (!(valueType === 'number' && defaultType === 'string')) {
                                  _context4.next = 16;
                                  break;
                                }

                                value = value.toString();
                                _context4.next = 17;
                                break;

                              case 16:
                                throw new Error("Set preference: \"".concat(value, "\" is a ").concat(valueType, ", ") + "expected a ".concat(defaultType, "."));

                              case 17:
                                _context4.next = 21;
                                break;

                              case 19:
                                if (!(valueType === 'number' && !Number.isInteger(value))) {
                                  _context4.next = 21;
                                  break;
                                }

                                throw new Error("Set preference: \"".concat(value, "\" must be an integer."));

                              case 21:
                                this.prefs[name] = value;
                                return _context4.abrupt("return", this._writeToStorage(this.prefs));

                              case 23:
                              case "end":
                                return _context4.stop();
                            }
                          }
                        }, _callee4, this);
                      }));

                  function set(_x3, _x4) {
                    return _set.apply(this, arguments);
                  }

                  return set;
                }()
              }, {
                key: "get",
                value: function () {
                  const _get = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee5(name) {
                        let defaultValue, prefValue;
                        return _regenerator["default"].wrap(function _callee5$(_context5) {
                          while (1) {
                            switch (_context5.prev = _context5.next) {
                              case 0:
                                _context5.next = 2;
                                return this._initializedPromise;

                              case 2:
                                defaultValue = this.defaults[name];

                                if (!(defaultValue === undefined)) {
                                  _context5.next = 7;
                                  break;
                                }

                                throw new Error("Get preference: \"".concat(name, "\" is undefined."));

                              case 7:
                                prefValue = this.prefs[name];

                                if (!(prefValue !== undefined)) {
                                  _context5.next = 10;
                                  break;
                                }

                                return _context5.abrupt("return", prefValue);

                              case 10:
                                return _context5.abrupt("return", defaultValue);

                              case 11:
                              case "end":
                                return _context5.stop();
                            }
                          }
                        }, _callee5, this);
                      }));

                  function get(_x5) {
                    return _get.apply(this, arguments);
                  }

                  return get;
                }()
              }, {
                key: "getAll",
                value: function () {
                  const _getAll = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee6() {
                        return _regenerator["default"].wrap(function _callee6$(_context6) {
                          while (1) {
                            switch (_context6.prev = _context6.next) {
                              case 0:
                                _context6.next = 2;
                                return this._initializedPromise;

                              case 2:
                                return _context6.abrupt("return", Object.assign(Object.create(null), this.defaults, this.prefs));

                              case 3:
                              case "end":
                                return _context6.stop();
                            }
                          }
                        }, _callee6, this);
                      }));

                  function getAll() {
                    return _getAll.apply(this, arguments);
                  }

                  return getAll;
                }()
              }]);

              return BasePreferences;
            }();

        exports.BasePreferences = BasePreferences;

/***/ }),
/* 38 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.DownloadManager = void 0;

        const _pdfjsLib = __webpack_require__(7);

        function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

;
        const DISABLE_CREATE_OBJECT_URL = _pdfjsLib.apiCompatibilityParams.disableCreateObjectURL || false;

        function _download(blobUrl, filename) {
  const a = document.createElement('a');

  if (!a.click) {
    throw new Error('DownloadManager: "a.click()" is not supported.');
  }

  a.href = blobUrl;
  a.target = '_parent';

  if ('download' in a) {
    a.download = filename;
  }

  (document.body || document.documentElement).appendChild(a);
  a.click();
  a.remove();
}

        const DownloadManager =
            /*#__PURE__*/
            function () {
              function DownloadManager(_ref) {
                const _ref$disableCreateObj = _ref.disableCreateObjectURL,
                    disableCreateObjectURL = _ref$disableCreateObj === void 0 ? DISABLE_CREATE_OBJECT_URL : _ref$disableCreateObj;

                _classCallCheck(this, DownloadManager);

                this.disableCreateObjectURL = disableCreateObjectURL;
              }

              _createClass(DownloadManager, [{
                key: "downloadUrl",
                value: function downloadUrl(url, filename) {
                  if (!(0, _pdfjsLib.createValidAbsoluteUrl)(url, 'http://example.com')) {
                    return;
                  }

                  _download(url + '#pdfjs.action=download', filename);
                }
              }, {
                key: "downloadData",
                value: function downloadData(data, filename, contentType) {
                  if (navigator.msSaveBlob) {
                    navigator.msSaveBlob(new Blob([data], {
                      type: contentType
                    }), filename);
                    return;
                  }

                  const blobUrl = (0, _pdfjsLib.createObjectURL)(data, contentType, this.disableCreateObjectURL);

                  _download(blobUrl, filename);
                }
              }, {
                key: "download",
                value: function download(blob, url, filename) {
                  if (navigator.msSaveBlob) {
                    if (!navigator.msSaveBlob(blob, filename)) {
                      this.downloadUrl(url, filename);
                    }

                    return;
                  }

                  if (this.disableCreateObjectURL) {
                    this.downloadUrl(url, filename);
                    return;
                  }

                  const blobUrl = URL.createObjectURL(blob);

                  _download(blobUrl, filename);
                }
              }]);

              return DownloadManager;
            }();

        exports.DownloadManager = DownloadManager;

/***/ }),
/* 39 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.GenericL10n = void 0;

        const _regenerator = _interopRequireDefault(__webpack_require__(2));

        __webpack_require__(40);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { Promise.resolve(value).then(_next, _throw); } }

function _asyncToGenerator(fn) { return function () {
  const self = this,
      args = arguments;
  return new Promise(function (resolve, reject) {
  const gen = fn.apply(self, args);

  function _next(value) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value); } function _throw(err) { asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err); } _next(undefined); }); }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (let i = 0; i < props.length; i++) {
  const descriptor = props[i];
  descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

        const webL10n = document.webL10n;

        const GenericL10n =
            /*#__PURE__*/
            function () {
              function GenericL10n(lang) {
                _classCallCheck(this, GenericL10n);

                this._lang = lang;
                this._ready = new Promise(function (resolve, reject) {
                  webL10n.setLanguage(lang, function () {
                    resolve(webL10n);
                  });
                });
              }

              _createClass(GenericL10n, [{
                key: "getLanguage",
                value: function () {
                  const _getLanguage = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee() {
                        let l10n;
                        return _regenerator["default"].wrap(function _callee$(_context) {
                          while (1) {
                            switch (_context.prev = _context.next) {
                              case 0:
                                _context.next = 2;
                                return this._ready;

                              case 2:
                                l10n = _context.sent;
                                return _context.abrupt("return", l10n.getLanguage());

                              case 4:
                              case "end":
                                return _context.stop();
                            }
                          }
                        }, _callee, this);
                      }));

                  function getLanguage() {
                    return _getLanguage.apply(this, arguments);
                  }

                  return getLanguage;
                }()
              }, {
                key: "getDirection",
                value: function () {
                  const _getDirection = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee2() {
                        let l10n;
                        return _regenerator["default"].wrap(function _callee2$(_context2) {
                          while (1) {
                            switch (_context2.prev = _context2.next) {
                              case 0:
                                _context2.next = 2;
                                return this._ready;

                              case 2:
                                l10n = _context2.sent;
                                return _context2.abrupt("return", l10n.getDirection());

                              case 4:
                              case "end":
                                return _context2.stop();
                            }
                          }
                        }, _callee2, this);
                      }));

                  function getDirection() {
                    return _getDirection.apply(this, arguments);
                  }

                  return getDirection;
                }()
              }, {
                key: "get",
                value: function () {
                  const _get = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee3(property, args, fallback) {
                        let l10n;
                        return _regenerator["default"].wrap(function _callee3$(_context3) {
                          while (1) {
                            switch (_context3.prev = _context3.next) {
                              case 0:
                                _context3.next = 2;
                                return this._ready;

                              case 2:
                                l10n = _context3.sent;
                                return _context3.abrupt("return", l10n.get(property, args, fallback));

                              case 4:
                              case "end":
                                return _context3.stop();
                            }
                          }
                        }, _callee3, this);
                      }));

                  function get(_x, _x2, _x3) {
                    return _get.apply(this, arguments);
                  }

                  return get;
                }()
              }, {
                key: "translate",
                value: function () {
                  const _translate = _asyncToGenerator(
                      /*#__PURE__*/
                      _regenerator["default"].mark(function _callee4(element) {
                        let l10n;
                        return _regenerator["default"].wrap(function _callee4$(_context4) {
                          while (1) {
                            switch (_context4.prev = _context4.next) {
                              case 0:
                                _context4.next = 2;
                                return this._ready;

                              case 2:
                                l10n = _context4.sent;
                                return _context4.abrupt("return", l10n.translate(element));

                              case 4:
                              case "end":
                                return _context4.stop();
                            }
                          }
                        }, _callee4, this);
                      }));

                  function translate(_x4) {
                    return _translate.apply(this, arguments);
                  }

                  return translate;
                }()
              }]);

              return GenericL10n;
            }();

        exports.GenericL10n = GenericL10n;

/***/ }),
/* 40 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


document.webL10n = function (window, document, undefined) {
  let gL10nData = {};
  let gTextData = '';
  const gTextProp = 'textContent';
  let gLanguage = '';
  const gMacros = {};
  let gReadyState = 'loading';
  const gAsyncResourceLoading = true;

  function getL10nResourceLinks() {
    return document.querySelectorAll('link[type="application/l10n"]');
  }

  function getL10nDictionary() {
    const script = document.querySelector('script[type="application/l10n"]');
    return script ? JSON.parse(script.innerHTML) : null;
  }

  function getTranslatableChildren(element) {
    return element ? element.querySelectorAll('*[data-l10n-id]') : [];
  }

  function getL10nAttributes(element) {
    if (!element) return {};
    const l10nId = element.getAttribute('data-l10n-id');
    const l10nArgs = element.getAttribute('data-l10n-args');
    let args = {};

    if (l10nArgs) {
      try {
        args = JSON.parse(l10nArgs);
      } catch (e) {
        console.warn('could not parse arguments for #' + l10nId);
      }
    }

    return {
      id: l10nId,
      args: args
    };
  }

  function xhrLoadText(url, onSuccess, onFailure) {
    onSuccess = onSuccess || function _onSuccess(data) {};

    onFailure = onFailure || function _onFailure() {};

    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, gAsyncResourceLoading);

    if (xhr.overrideMimeType) {
      xhr.overrideMimeType('text/plain; charset=utf-8');
    }

    xhr.onreadystatechange = function () {
      if (xhr.readyState == 4) {
        if (xhr.status == 200 || xhr.status === 0) {
          onSuccess(xhr.responseText);
        } else {
          onFailure();
        }
      }
    };

    xhr.onerror = onFailure;
    xhr.ontimeout = onFailure;

    try {
      xhr.send(null);
    } catch (e) {
      onFailure();
    }
  }

  function parseResource(href, lang, successCallback, failureCallback) {
    const baseURL = href.replace(/[^\/]*$/, '') || './';

    function evalString(text) {
      if (text.lastIndexOf('\\') < 0) return text;
      return text.replace(/\\\\/g, '\\').replace(/\\n/g, '\n').replace(/\\r/g, '\r').replace(/\\t/g, '\t').replace(/\\b/g, '\b').replace(/\\f/g, '\f').replace(/\\{/g, '{').replace(/\\}/g, '}').replace(/\\"/g, '"').replace(/\\'/g, "'");
    }

    function parseProperties(text, parsedPropertiesCallback) {
      const dictionary = {};
      const reBlank = /^\s*|\s*$/;
      const reComment = /^\s*#|^\s*$/;
      const reSection = /^\s*\[(.*)\]\s*$/;
      const reImport = /^\s*@import\s+url\((.*)\)\s*$/i;
      const reSplit = /^([^=\s]*)\s*=\s*(.+)$/;

      function parseRawLines(rawText, extendedSyntax, parsedRawLinesCallback) {
        const entries = rawText.replace(reBlank, '').split(/[\r\n]+/);
        let currentLang = '*';
        const genericLang = lang.split('-', 1)[0];
        let skipLang = false;
        let match = '';

        function nextEntry() {
          while (true) {
            if (!entries.length) {
              parsedRawLinesCallback();
              return;
            }

            const line = entries.shift();
            if (reComment.test(line)) continue;

            if (extendedSyntax) {
              match = reSection.exec(line);

              if (match) {
                currentLang = match[1].toLowerCase();
                skipLang = currentLang !== '*' && currentLang !== lang && currentLang !== genericLang;
                continue;
              } else if (skipLang) {
                continue;
              }

              match = reImport.exec(line);

              if (match) {
                loadImport(baseURL + match[1], nextEntry);
                return;
              }
            }

            const tmp = line.match(reSplit);

            if (tmp && tmp.length == 3) {
              dictionary[tmp[1]] = evalString(tmp[2]);
            }
          }
        }

        nextEntry();
      }

      function loadImport(url, callback) {
        xhrLoadText(url, function (content) {
          parseRawLines(content, false, callback);
        }, function () {
          console.warn(url + ' not found.');
          callback();
        });
      }

      parseRawLines(text, true, function () {
        parsedPropertiesCallback(dictionary);
      });
    }

    xhrLoadText(href, function (response) {
      gTextData += response;
      parseProperties(response, function (data) {
        for (let key in data) {
          let id,
              prop;
          const index = key.lastIndexOf('.');

          if (index > 0) {
            id = key.substring(0, index);
            prop = key.substring(index + 1);
          } else {
            id = key;
            prop = gTextProp;
          }

          if (!gL10nData[id]) {
            gL10nData[id] = {};
          }

          gL10nData[id][prop] = data[key];
        }

        if (successCallback) {
          successCallback();
        }
      });
    }, failureCallback);
  }

  function loadLocale(lang, callback) {
    if (lang) {
      lang = lang.toLowerCase();
    }

    callback = callback || function _callback() {};

    clear();
    gLanguage = lang;
    const langLinks = getL10nResourceLinks();
    const langCount = langLinks.length;

    if (langCount === 0) {
      const dict = getL10nDictionary();

      if (dict && dict.locales && dict.default_locale) {
        console.log('using the embedded JSON directory, early way out');
        gL10nData = dict.locales[lang];

        if (!gL10nData) {
          const defaultLocale = dict.default_locale.toLowerCase();

          for (let anyCaseLang in dict.locales) {
            anyCaseLang = anyCaseLang.toLowerCase();

            if (anyCaseLang === lang) {
              gL10nData = dict.locales[lang];
              break;
            } else if (anyCaseLang === defaultLocale) {
              gL10nData = dict.locales[defaultLocale];
            }
          }
        }

        callback();
      } else {
        console.log('no resource to load, early way out');
      }

      gReadyState = 'complete';
      return;
    }

    let onResourceLoaded = null;
    let gResourceCount = 0;

    onResourceLoaded = function onResourceLoaded() {
      gResourceCount++;

      if (gResourceCount >= langCount) {
        callback();
        gReadyState = 'complete';
      }
    };

    function L10nResourceLink(link) {
      const href = link.href;

      this.load = function (lang, callback) {
        parseResource(href, lang, callback, function () {
          console.warn(href + ' not found.');
          console.warn('"' + lang + '" resource not found');
          gLanguage = '';
          callback();
        });
      };
    }

    for (let i = 0; i < langCount; i++) {
      const resource = new L10nResourceLink(langLinks[i]);
      resource.load(lang, onResourceLoaded);
    }
  }

  function clear() {
    gL10nData = {};
    gTextData = '';
    gLanguage = '';
  }

  function getPluralRules(lang) {
    const locales2rules = {
      'af': 3,
      'ak': 4,
      'am': 4,
      'ar': 1,
      'asa': 3,
      'az': 0,
      'be': 11,
      'bem': 3,
      'bez': 3,
      'bg': 3,
      'bh': 4,
      'bm': 0,
      'bn': 3,
      'bo': 0,
      'br': 20,
      'brx': 3,
      'bs': 11,
      'ca': 3,
      'cgg': 3,
      'chr': 3,
      'cs': 12,
      'cy': 17,
      'da': 3,
      'de': 3,
      'dv': 3,
      'dz': 0,
      'ee': 3,
      'el': 3,
      'en': 3,
      'eo': 3,
      'es': 3,
      'et': 3,
      'eu': 3,
      'fa': 0,
      'ff': 5,
      'fi': 3,
      'fil': 4,
      'fo': 3,
      'fr': 5,
      'fur': 3,
      'fy': 3,
      'ga': 8,
      'gd': 24,
      'gl': 3,
      'gsw': 3,
      'gu': 3,
      'guw': 4,
      'gv': 23,
      'ha': 3,
      'haw': 3,
      'he': 2,
      'hi': 4,
      'hr': 11,
      'hu': 0,
      'id': 0,
      'ig': 0,
      'ii': 0,
      'is': 3,
      'it': 3,
      'iu': 7,
      'ja': 0,
      'jmc': 3,
      'jv': 0,
      'ka': 0,
      'kab': 5,
      'kaj': 3,
      'kcg': 3,
      'kde': 0,
      'kea': 0,
      'kk': 3,
      'kl': 3,
      'km': 0,
      'kn': 0,
      'ko': 0,
      'ksb': 3,
      'ksh': 21,
      'ku': 3,
      'kw': 7,
      'lag': 18,
      'lb': 3,
      'lg': 3,
      'ln': 4,
      'lo': 0,
      'lt': 10,
      'lv': 6,
      'mas': 3,
      'mg': 4,
      'mk': 16,
      'ml': 3,
      'mn': 3,
      'mo': 9,
      'mr': 3,
      'ms': 0,
      'mt': 15,
      'my': 0,
      'nah': 3,
      'naq': 7,
      'nb': 3,
      'nd': 3,
      'ne': 3,
      'nl': 3,
      'nn': 3,
      'no': 3,
      'nr': 3,
      'nso': 4,
      'ny': 3,
      'nyn': 3,
      'om': 3,
      'or': 3,
      'pa': 3,
      'pap': 3,
      'pl': 13,
      'ps': 3,
      'pt': 3,
      'rm': 3,
      'ro': 9,
      'rof': 3,
      'ru': 11,
      'rwk': 3,
      'sah': 0,
      'saq': 3,
      'se': 7,
      'seh': 3,
      'ses': 0,
      'sg': 0,
      'sh': 11,
      'shi': 19,
      'sk': 12,
      'sl': 14,
      'sma': 7,
      'smi': 7,
      'smj': 7,
      'smn': 7,
      'sms': 7,
      'sn': 3,
      'so': 3,
      'sq': 3,
      'sr': 11,
      'ss': 3,
      'ssy': 3,
      'st': 3,
      'sv': 3,
      'sw': 3,
      'syr': 3,
      'ta': 3,
      'te': 3,
      'teo': 3,
      'th': 0,
      'ti': 4,
      'tig': 3,
      'tk': 3,
      'tl': 4,
      'tn': 3,
      'to': 0,
      'tr': 0,
      'ts': 3,
      'tzm': 22,
      'uk': 11,
      'ur': 3,
      've': 3,
      'vi': 0,
      'vun': 3,
      'wa': 4,
      'wae': 3,
      'wo': 0,
      'xh': 3,
      'xog': 3,
      'yo': 0,
      'zh': 0,
      'zu': 3
    };

    function isIn(n, list) {
      return list.indexOf(n) !== -1;
    }

    function isBetween(n, start, end) {
      return start <= n && n <= end;
    }

    const pluralRules = {
      '0': function _(n) {
        return 'other';
      },
      '1': function _(n) {
        if (isBetween(n % 100, 3, 10)) return 'few';
        if (n === 0) return 'zero';
        if (isBetween(n % 100, 11, 99)) return 'many';
        if (n == 2) return 'two';
        if (n == 1) return 'one';
        return 'other';
      },
      '2': function _(n) {
        if (n !== 0 && n % 10 === 0) return 'many';
        if (n == 2) return 'two';
        if (n == 1) return 'one';
        return 'other';
      },
      '3': function _(n) {
        if (n == 1) return 'one';
        return 'other';
      },
      '4': function _(n) {
        if (isBetween(n, 0, 1)) return 'one';
        return 'other';
      },
      '5': function _(n) {
        if (isBetween(n, 0, 2) && n != 2) return 'one';
        return 'other';
      },
      '6': function _(n) {
        if (n === 0) return 'zero';
        if (n % 10 == 1 && n % 100 != 11) return 'one';
        return 'other';
      },
      '7': function _(n) {
        if (n == 2) return 'two';
        if (n == 1) return 'one';
        return 'other';
      },
      '8': function _(n) {
        if (isBetween(n, 3, 6)) return 'few';
        if (isBetween(n, 7, 10)) return 'many';
        if (n == 2) return 'two';
        if (n == 1) return 'one';
        return 'other';
      },
      '9': function _(n) {
        if (n === 0 || n != 1 && isBetween(n % 100, 1, 19)) return 'few';
        if (n == 1) return 'one';
        return 'other';
      },
      '10': function _(n) {
        if (isBetween(n % 10, 2, 9) && !isBetween(n % 100, 11, 19)) return 'few';
        if (n % 10 == 1 && !isBetween(n % 100, 11, 19)) return 'one';
        return 'other';
      },
      '11': function _(n) {
        if (isBetween(n % 10, 2, 4) && !isBetween(n % 100, 12, 14)) return 'few';
        if (n % 10 === 0 || isBetween(n % 10, 5, 9) || isBetween(n % 100, 11, 14)) return 'many';
        if (n % 10 == 1 && n % 100 != 11) return 'one';
        return 'other';
      },
      '12': function _(n) {
        if (isBetween(n, 2, 4)) return 'few';
        if (n == 1) return 'one';
        return 'other';
      },
      '13': function _(n) {
        if (isBetween(n % 10, 2, 4) && !isBetween(n % 100, 12, 14)) return 'few';
        if (n != 1 && isBetween(n % 10, 0, 1) || isBetween(n % 10, 5, 9) || isBetween(n % 100, 12, 14)) return 'many';
        if (n == 1) return 'one';
        return 'other';
      },
      '14': function _(n) {
        if (isBetween(n % 100, 3, 4)) return 'few';
        if (n % 100 == 2) return 'two';
        if (n % 100 == 1) return 'one';
        return 'other';
      },
      '15': function _(n) {
        if (n === 0 || isBetween(n % 100, 2, 10)) return 'few';
        if (isBetween(n % 100, 11, 19)) return 'many';
        if (n == 1) return 'one';
        return 'other';
      },
      '16': function _(n) {
        if (n % 10 == 1 && n != 11) return 'one';
        return 'other';
      },
      '17': function _(n) {
        if (n == 3) return 'few';
        if (n === 0) return 'zero';
        if (n == 6) return 'many';
        if (n == 2) return 'two';
        if (n == 1) return 'one';
        return 'other';
      },
      '18': function _(n) {
        if (n === 0) return 'zero';
        if (isBetween(n, 0, 2) && n !== 0 && n != 2) return 'one';
        return 'other';
      },
      '19': function _(n) {
        if (isBetween(n, 2, 10)) return 'few';
        if (isBetween(n, 0, 1)) return 'one';
        return 'other';
      },
      '20': function _(n) {
        if ((isBetween(n % 10, 3, 4) || n % 10 == 9) && !(isBetween(n % 100, 10, 19) || isBetween(n % 100, 70, 79) || isBetween(n % 100, 90, 99))) return 'few';
        if (n % 1000000 === 0 && n !== 0) return 'many';
        if (n % 10 == 2 && !isIn(n % 100, [12, 72, 92])) return 'two';
        if (n % 10 == 1 && !isIn(n % 100, [11, 71, 91])) return 'one';
        return 'other';
      },
      '21': function _(n) {
        if (n === 0) return 'zero';
        if (n == 1) return 'one';
        return 'other';
      },
      '22': function _(n) {
        if (isBetween(n, 0, 1) || isBetween(n, 11, 99)) return 'one';
        return 'other';
      },
      '23': function _(n) {
        if (isBetween(n % 10, 1, 2) || n % 20 === 0) return 'one';
        return 'other';
      },
      '24': function _(n) {
        if (isBetween(n, 3, 10) || isBetween(n, 13, 19)) return 'few';
        if (isIn(n, [2, 12])) return 'two';
        if (isIn(n, [1, 11])) return 'one';
        return 'other';
      }
    };
    const index = locales2rules[lang.replace(/-.*$/, '')];

    if (!(index in pluralRules)) {
      console.warn('plural form unknown for [' + lang + ']');
      return function () {
        return 'other';
      };
    }

    return pluralRules[index];
  }

  gMacros.plural = function (str, param, key, prop) {
    const n = parseFloat(param);
    if (isNaN(n)) return str;
    if (prop != gTextProp) return str;

    if (!gMacros._pluralRules) {
      gMacros._pluralRules = getPluralRules(gLanguage);
    }

    const index = '[' + gMacros._pluralRules(n) + ']';

    if (n === 0 && key + '[zero]' in gL10nData) {
      str = gL10nData[key + '[zero]'][prop];
    } else if (n == 1 && key + '[one]' in gL10nData) {
      str = gL10nData[key + '[one]'][prop];
    } else if (n == 2 && key + '[two]' in gL10nData) {
      str = gL10nData[key + '[two]'][prop];
    } else if (key + index in gL10nData) {
      str = gL10nData[key + index][prop];
    } else if (key + '[other]' in gL10nData) {
      str = gL10nData[key + '[other]'][prop];
    }

    return str;
  };

  function getL10nData(key, args, fallback) {
    let data = gL10nData[key];

    if (!data) {
      // todo this here is commented to remove annoying messages, should not be a bug
      // console.warn('#' + key + ' is undefined.');

      if (!fallback) {
        return null;
      }

      data = fallback;
    }

    const rv = {};

    for (let prop in data) {
      let str = data[prop];
      str = substIndexes(str, args, key, prop);
      str = substArguments(str, args, key);
      rv[prop] = str;
    }

    return rv;
  }

  function substIndexes(str, args, key, prop) {
    const reIndex = /\{\[\s*([a-zA-Z]+)\(([a-zA-Z]+)\)\s*\]\}/;
    const reMatch = reIndex.exec(str);
    if (!reMatch || !reMatch.length) return str;
    const macroName = reMatch[1];
    const paramName = reMatch[2];
    let param;

    if (args && paramName in args) {
      param = args[paramName];
    } else if (paramName in gL10nData) {
      param = gL10nData[paramName];
    }

    if (macroName in gMacros) {
      const macro = gMacros[macroName];
      str = macro(str, param, key, prop);
    }

    return str;
  }

  function substArguments(str, args, key) {
    const reArgs = /\{\{\s*(.+?)\s*\}\}/g;
    return str.replace(reArgs, function (matched_text, arg) {
      if (args && arg in args) {
        return args[arg];
      }

      if (arg in gL10nData) {
        return gL10nData[arg];
      }

      console.log('argument {{' + arg + '}} for #' + key + ' is undefined.');
      return matched_text;
    });
  }

  function translateElement(element) {
    const l10n = getL10nAttributes(element);
    if (!l10n.id) return;
    const data = getL10nData(l10n.id, l10n.args);

    if (!data) {
      // todo this here is commented to remove annoying messages, should not be a bug
      // console.warn('#' + l10n.id + ' is undefined.');
      return;
    }

    if (data[gTextProp]) {
      if (getChildElementCount(element) === 0) {
        element[gTextProp] = data[gTextProp];
      } else {
        const children = element.childNodes;
        let found = false;

        let i = 0;
        const l = children.length;
        for (; i < l; i++) {
          if (children[i].nodeType === 3 && /\S/.test(children[i].nodeValue)) {
            if (found) {
              children[i].nodeValue = '';
            } else {
              children[i].nodeValue = data[gTextProp];
              found = true;
            }
          }
        }

        if (!found) {
          const textNode = document.createTextNode(data[gTextProp]);
          element.insertBefore(textNode, element.firstChild);
        }
      }

      delete data[gTextProp];
    }

    for (let k in data) {
      element[k] = data[k];
    }
  }

  function getChildElementCount(element) {
    if (element.children) {
      return element.children.length;
    }

    if (typeof element.childElementCount !== 'undefined') {
      return element.childElementCount;
    }

    let count = 0;

    for (let i = 0; i < element.childNodes.length; i++) {
      count += element.nodeType === 1 ? 1 : 0;
    }

    return count;
  }

  function translateFragment(element) {
    element = element || document.documentElement;
    const children = getTranslatableChildren(element);
    const elementCount = children.length;

    for (let i = 0; i < elementCount; i++) {
      translateElement(children[i]);
    }

    translateElement(element);
  }

  return {
    get: function get(key, args, fallbackString) {
      const index = key.lastIndexOf('.');
      let prop = gTextProp;

      if (index > 0) {
        prop = key.substring(index + 1);
        key = key.substring(0, index);
      }

      let fallback;

      if (fallbackString) {
        fallback = {};
        fallback[prop] = fallbackString;
      }

      const data = getL10nData(key, args, fallback);

      if (data && prop in data) {
        return data[prop];
      }

      return '{{' + key + '}}';
    },
    getData: function getData() {
      return gL10nData;
    },
    getText: function getText() {
      return gTextData;
    },
    getLanguage: function getLanguage() {
      return gLanguage;
    },
    setLanguage: function setLanguage(lang, callback) {
      loadLocale(lang, function () {
        if (callback) callback();
      });
    },
    getDirection: function getDirection() {
      const rtlList = ['ar', 'he', 'fa', 'ps', 'ur'];
      const shortCode = gLanguage.split('-', 1)[0];
      return rtlList.indexOf(shortCode) >= 0 ? 'rtl' : 'ltr';
    },
    translate: translateFragment,
    getReadyState: function getReadyState() {
      return gReadyState;
    },
    ready: function ready(callback) {
      if (!callback) {
        return;
      } else if (gReadyState == 'complete' || gReadyState == 'interactive') {
        window.setTimeout(function () {
          callback();
        });
      } else if (document.addEventListener) {
        document.addEventListener('localized', function once() {
          document.removeEventListener('localized', once);
          callback();
        });
      }
    }
  };
}(window, document);

/***/ }),
/* 41 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PDFPrintService = PDFPrintService;

        const _ui_utils = __webpack_require__(5);

        const _app = __webpack_require__(1);

        const _app_options = __webpack_require__(6);

        let activeService = null;
        let overlayManager = null;

        function renderPage(activeServiceOnEntry, pdfDocument, pageNumber, size) {
  const scratchCanvas = activeService.scratchCanvas;
  const PRINT_RESOLUTION = _app_options.AppOptions.get('printResolution') || 150;
  const PRINT_UNITS = PRINT_RESOLUTION / 72.0;
  scratchCanvas.width = Math.floor(size.width * PRINT_UNITS);
  scratchCanvas.height = Math.floor(size.height * PRINT_UNITS);
  const width = Math.floor(size.width * _ui_utils.CSS_UNITS) + 'px';
  const height = Math.floor(size.height * _ui_utils.CSS_UNITS) + 'px';
  const ctx = scratchCanvas.getContext('2d');
  ctx.save();
  ctx.fillStyle = 'rgb(255, 255, 255)';
  ctx.fillRect(0, 0, scratchCanvas.width, scratchCanvas.height);
  ctx.restore();
  return pdfDocument.getPage(pageNumber).then(function (pdfPage) {
    const renderContext = {
      canvasContext: ctx,
      transform: [PRINT_UNITS, 0, 0, PRINT_UNITS, 0, 0],
      viewport: pdfPage.getViewport({
        scale: 1,
        rotation: size.rotation
      }),
      intent: 'print'
    };
    return pdfPage.render(renderContext).promise;
  }).then(function () {
    return {
      width: width,
      height: height
    };
  });
}

function PDFPrintService(pdfDocument, pagesOverview, printContainer, l10n) {
  this.pdfDocument = pdfDocument;
  this.pagesOverview = pagesOverview;
  this.printContainer = printContainer;
  this.l10n = l10n || _ui_utils.NullL10n;
  this.disableCreateObjectURL = pdfDocument.loadingParams['disableCreateObjectURL'];
  this.currentPage = -1;
  this.scratchCanvas = document.createElement('canvas');
}

PDFPrintService.prototype = {
  layout: function layout() {
    this.throwIfInactive();
    const body = document.querySelector('body');
    body.setAttribute('data-pdfjsprinting', true);
    const hasEqualPageSizes = this.pagesOverview.every(function (size) {
      return size.width === this.pagesOverview[0].width && size.height === this.pagesOverview[0].height;
    }, this);

    if (!hasEqualPageSizes) {
      console.warn('Not all pages have the same size. The printed ' + 'result may be incorrect!');
    }

    this.pageStyleSheet = document.createElement('style');
    const pageSize = this.pagesOverview[0];
    this.pageStyleSheet.textContent = '@supports ((size:A4) and (size:1pt 1pt)) {' + '@page { size: ' + pageSize.width + 'pt ' + pageSize.height + 'pt;}' + '}';
    body.appendChild(this.pageStyleSheet);
  },
  destroy: function destroy() {
    if (activeService !== this) {
      return;
    }

    this.printContainer.textContent = '';
    const body = document.querySelector('body');
    body.removeAttribute('data-pdfjsprinting');

    if (this.pageStyleSheet) {
      this.pageStyleSheet.remove();
      this.pageStyleSheet = null;
    }

    this.scratchCanvas.width = this.scratchCanvas.height = 0;
    this.scratchCanvas = null;
    activeService = null;
    ensureOverlay().then(function () {
      if (overlayManager.active !== 'printServiceOverlay') {
        return;
      }

      overlayManager.close('printServiceOverlay');
    });
  },
  renderPages: function renderPages() {
    const _this = this;

    const pageCount = this.pagesOverview.length;

    const renderNextPage = function renderNextPage(resolve, reject) {
      _this.throwIfInactive();

      if (++_this.currentPage >= pageCount) {
        renderProgress(pageCount, pageCount, _this.l10n);
        resolve();
        return;
      }

      const index = _this.currentPage;
      renderProgress(index, pageCount, _this.l10n);
      renderPage(_this, _this.pdfDocument, index + 1, _this.pagesOverview[index]).then(_this.useRenderedPage.bind(_this)).then(function () {
        renderNextPage(resolve, reject);
      }, reject);
    };

    return new Promise(renderNextPage);
  },
  useRenderedPage: function useRenderedPage(printItem) {
    this.throwIfInactive();
    const img = document.createElement('img');
    img.style.width = printItem.width;
    img.style.height = printItem.height;
    const scratchCanvas = this.scratchCanvas;

    if ('toBlob' in scratchCanvas && !this.disableCreateObjectURL) {
      scratchCanvas.toBlob(function (blob) {
        img.src = URL.createObjectURL(blob);
      });
    } else {
      img.src = scratchCanvas.toDataURL();
    }

    const wrapper = document.createElement('div');
    wrapper.appendChild(img);
    this.printContainer.appendChild(wrapper);
    return new Promise(function (resolve, reject) {
      img.onload = resolve;
      img.onerror = reject;
    });
  },
  performPrint: function performPrint() {
    const _this2 = this;

    this.throwIfInactive();
    return new Promise(function (resolve) {
      setTimeout(function () {
        if (!_this2.active) {
          resolve();
          return;
        }

        print.call(window);
        setTimeout(resolve, 20);
      }, 0);
    });
  },

  get active() {
    return this === activeService;
  },

  throwIfInactive: function throwIfInactive() {
    if (!this.active) {
      throw new Error('This print request was cancelled or completed.');
    }
  }
};
var print = window.print;

window.print = function print() {
  if (activeService) {
    console.warn('Ignored window.print() because of a pending print job.');
    return;
  }

  ensureOverlay().then(function () {
    if (activeService) {
      overlayManager.open('printServiceOverlay');
    }
  });

  try {
    dispatchEvent('beforeprint');
  } finally {
    if (!activeService) {
      console.error('Expected print service to be initialized.');
      ensureOverlay().then(function () {
        if (overlayManager.active === 'printServiceOverlay') {
          overlayManager.close('printServiceOverlay');
        }
      });
      return;
    }

    const activeServiceOnEntry = activeService;
    activeService.renderPages().then(function () {
      return activeServiceOnEntry.performPrint();
    })["catch"](function () {}).then(function () {
      if (activeServiceOnEntry.active) {
        abort();
      }
    });
  }
};

function dispatchEvent(eventType) {
  const event = document.createEvent('CustomEvent');
  event.initCustomEvent(eventType, false, false, 'custom');
  window.dispatchEvent(event);
}

function abort() {
  if (activeService) {
    activeService.destroy();
    dispatchEvent('afterprint');
  }
}

function renderProgress(index, total, l10n) {
  const progressContainer = document.getElementById('printServiceOverlay');
  const progress = Math.round(100 * index / total);
  const progressBar = progressContainer.querySelector('progress');
  const progressPerc = progressContainer.querySelector('.relative-progress');
  progressBar.value = progress;
  l10n.get('print_progress_percent', {
    progress: progress
  }, progress + '%').then(function (msg) {
    progressPerc.textContent = msg;
  });
}

        const hasAttachEvent = !!document.attachEvent;
        window.addEventListener('keydown', function (event) {
  if (event.keyCode === 80 && (event.ctrlKey || event.metaKey) && !event.altKey && (!event.shiftKey || window.chrome || window.opera)) {
    window.print();

    if (hasAttachEvent) {
      return;
    }

    event.preventDefault();

    if (event.stopImmediatePropagation) {
      event.stopImmediatePropagation();
    } else {
      event.stopPropagation();
    }
  }
}, true);

if (hasAttachEvent) {
  document.attachEvent('onkeydown', function (event) {
    event = event || window.event;

    if (event.keyCode === 80 && event.ctrlKey) {
      event.keyCode = 0;
      return false;
    }
  });
}

if ('onbeforeprint' in window) {
  const stopPropagationIfNeeded = function stopPropagationIfNeeded(event) {
    if (event.detail !== 'custom' && event.stopImmediatePropagation) {
      event.stopImmediatePropagation();
    }
  };

  window.addEventListener('beforeprint', stopPropagationIfNeeded);
  window.addEventListener('afterprint', stopPropagationIfNeeded);
}

        let overlayPromise;

        function ensureOverlay() {
  if (!overlayPromise) {
    overlayManager = _app.PDFViewerApplication.overlayManager;

    if (!overlayManager) {
      throw new Error('The overlay manager has not yet been initialized.');
    }

    overlayPromise = overlayManager.register('printServiceOverlay', document.getElementById('printServiceOverlay'), abort, true);
    document.getElementById('printCancel').onclick = abort;
  }

  return overlayPromise;
}

_app.PDFPrintServiceFactory.instance = {
  supportsPrinting: true,
  createPrintService: function createPrintService(pdfDocument, pagesOverview, printContainer, l10n) {
    if (activeService) {
      throw new Error('The print service is created and active.');
    }

    activeService = new PDFPrintService(pdfDocument, pagesOverview, printContainer, l10n);
    return activeService;
  }
};

/***/ })
 ]);
//# sourceMappingURL=viewer.js.map