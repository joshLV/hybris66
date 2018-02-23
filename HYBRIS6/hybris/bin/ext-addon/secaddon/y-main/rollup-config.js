import rollup      from 'rollup'
import nodeResolve from 'rollup-plugin-node-resolve'
import commonjs    from 'rollup-plugin-commonjs';
import uglify      from 'rollup-plugin-uglify'

//paths are relative to the execution path
export default {
  entry: `aot/src/main-aot.js`,
  dest: `../acceleratoraddon/web/webroot/_ui/responsive/common/angular/build-${process.env.locale}.js`, // output a single application bundle
  sourceMap: true,
  sourceMapFile: 'aot/dist/build.js.map',
  format: 'iife',
  onwarn: function(warning) {
    // Skip certain warnings

    // should intercept ... but doesn't in some rollup versions
    if ( warning.code === 'THIS_IS_UNDEFINED' ) { return; }

    // console.warn everything else
    console.warn( warning.message );
  },
  plugins: [
    nodeResolve({jsnext: true, module: true}),
    commonjs({
              include: [(process.env.ANGULAR_ANCILLARY_NODE_MODULES_PATH || 'node_modules') + '/rxjs/**']
    }),
    uglify()
  ]
}
