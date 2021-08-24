const path = require('path');

module.exports = {
  mode: 'development',
  entry: path.resolve(__dirname, 'src/pages/index.js'),
  output: {
    path: __dirname,
    filename: "main.js"
  },
  resolve: {
    extensions: [".js", ".marko"]
  },
  module: {
    rules: [
      {
        test: /\.marko$/,
        loader: "@marko/webpack/loader"
      },
    ]
  },
};
