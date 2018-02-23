module.exports = {
    "devtool": "source-map",
    "externals": {
        "jasmine": "jasmine",
        "testutils": "testutils",
        "angular-mocks": "angular-mocks",
        "angular": "angular",
        "angular-route": "angular-route",
        "angular-translate": "angular-translate"
    },
    "output": {
        "path": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002FjsTarget",
        "filename": "[name].js",
        "sourceMapFilename": "[file].map"
    },
    "resolve": {
        "modules": [
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002FjsTarget\u002Fweb\u002Fapp",
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002FjsTarget\u002Fweb\u002Ffeatures"
        ],
        "extensions": [
            ".ts",
            ".js"
        ],
        "alias": {
            "cmscommons": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002FjsTarget\u002Fweb\u002Ffeatures\u002Fcmscommons",
            "cmssmartedit": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002FjsTarget\u002Fweb\u002Ffeatures\u002Fcmssmartedit"
        }
    },
    "module": {
        "rules": [{
            "test": /\.ts$/,
            "loader": "awesome-typescript-loader",
            "options": {
                "configFileName": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fcmssmartedit\u002Fsmartedit-custom-build\u002Fgenerated\u002Ftsconfig.dev.smartedit.json"
            }
        }]
    },
    "stats": {
        "colors": true,
        "modules": true,
        "reasons": true,
        "errorDetails": true
    },
    "bail": false,
    "entry": {
        "cmssmartedit": ".\u002FjsTarget\u002Fweb\u002Ffeatures\u002Fcmssmartedit\u002FcmssmarteditApp.ts"
    }
};
