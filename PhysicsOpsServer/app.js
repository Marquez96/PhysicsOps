var express         = require("express"),
    app             = express(),
    bodyParser      = require("body-parser"),
    methodOverride  = require("method-override"),
    mongoose        = require('mongoose');

// Connection to DB
mongoose.connect('mongodb://192.168.191.132:27017/physicsOps', function(err, res) {
  if(err) throw err;
  console.log('Connected to Database');
});

// Middlewares
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(methodOverride());

// Import Models and controllers
var models2 = require('./models/usermodel')(app, mongoose);
var userCtrl = require('./controllers/users');

// Example Route
var router = express.Router();

app.use('/api', express.static('apidoc'));

app.use(router);

router.route('/users')
  .get(userCtrl.findAllUsers)
  .post(userCtrl.addUser)
  .put(userCtrl.updateUser);

router.route('/users/:userName/:pwd')
  .get(userCtrl.findById);

router.route('/checkUser/:userName')
  .get(userCtrl.checkUser);

router.route('/friend/:userName/:pwd')
  .post(userCtrl.addFriend)
  .delete(userCtrl.deleteFriend)
  .put(userCtrl.updateFriend);



app.use('/api', router);

// Start server
app.listen(3000, function() {
  console.log("Node server running on http://localhost:3000");
});
