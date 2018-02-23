module.exports = {
    "devtool": "none",
    "externals": {
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
                    "configFileName": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fsmartedit\u002Fsmartedit-custom-build\u002Fgenerated\u002Ftsconfig.prod.smartedit.json"
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
    "bail": false,
    "entry": {
        "presmartedit": ".\u002FjsTarget\u002Fweb\u002Fapp\u002Fsmartedit\u002Findex.ts",
        "postsmartedit": ".\u002FjsTarget\u002Fweb\u002Fapp\u002Fsmartedit\u002Fcore\u002Fsmarteditbootstrap.ts",
        "systemModule": ".\u002FjsTarget\u002Fweb\u002Fapp\u002Fsmartedit\u002Fmodules\u002FsystemModule\u002Fsystem.ts"
    }
};