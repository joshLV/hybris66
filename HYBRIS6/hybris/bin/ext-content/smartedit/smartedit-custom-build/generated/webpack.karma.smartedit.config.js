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
        "path": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fsmartedit\u002FjsTarget",
        "filename": "[name].js",
        "sourceMapFilename": "[file].map"
    },
    "resolve": {
        "modules": [
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fsmartedit\u002FjsTarget\u002Fweb\u002Fapp",
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fsmartedit\u002FjsTarget\u002Fweb\u002Ffeatures"
        ],
        "extensions": [
            ".ts",
            ".js"
        ]
    },
    "module": {
        "rules": [
            {
                "test": /\.ts$/,
                "loader": "awesome-typescript-loader",
                "options": {
                    "configFileName": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fsmartedit\u002Fsmartedit-custom-build\u002Fgenerated\u002Ftsconfig.karma.smartedit.json"
                }
            }
        ]
    },
    "stats": {
        "colors": true,
        "modules": true,
        "reasons": true,
        "errorDetails": true
    },
    "bail": false
};