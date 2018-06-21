//File: controllers/users.js
var mongoose = require('mongoose');
var modelUser = mongoose.model('users');

//GET - Return all tvshows in the DB
exports.findAllUsers = function (req, res) {
    modelUser.find(function (err, usersDB) {
        if (err)
            res.send(500, err.message);
        console.log('GET /users')
        res.status(200).jsonp(usersDB);
    });
};

/**
 * @api {post} /user/:id Insert user
 * @apiName InsertUser
 * @apiGroup User
 * 
 * @apiParamExample {json} Request-Example:
 * {
 *   "events": [
 *     {
 *       "cal": {
 *         "year": 2018,
 *         "month": 4,
 *         "dayOfMonth": 9,
 *         "hourOfDay": 0,
 *         "minute": 0,
 *         "second": 0
 *       },
 *       "day": 9,
 *       "duration": 1,
 *       "id": 1,
 *       "importance": 1,
 *       "month": 4,
 *       "title": "test",
 *       "year": 2018
 *     }
 *   ],
 *   "friends": [
 *     {
 *       "id": 30,
 *       "idUserFrom": "testUserFrom",
 *       "idUserTo": "testUserTo",
 *       "state": "friend"
 *     },
 *     {
 *       "id": 34,
 *       "idUserFrom": "admin",
 *       "idUserTo": "carlos",
 *       "state": "friend"
 *     }
 *   ],
 *   "idUser": 123,
 *   "mail": "test@mail.com",
 *   "name": "test",
 *   "password": "test",
 *   "surname": "test",
 *   "userName": "test"
 * }
 * 
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *        result: "Inserted"
 *     }
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiVersion 1.0.0
 * */
exports.addUser = function (req, res) {
    console.log('POST /users');
    modelUser.findOne({ "userName": req.body.userName }, function (err, userDB) {
        if (userDB === null || userDB === undefined) {
            var us = new modelUser({
                events: req.body.events,
                friends: req.body.friends,
                idUser: req.body.idUser,
                mail: req.body.mail,
                name: req.body.name,
                password: req.body.password,
                surname: req.body.surname,
                userName: req.body.userName,
                lastUpdate: req.body.lastUpdate
            });

            us.save(function (err, us) {
                if (err) return res.send(500, err.message);
                res.status(200).jsonp({ result: "Inserted" });
            });
        } else {
            res.status(401).jsonp({ result: "User exists" });
        }
    });

};

/**
 * @api {get} /user/:username/:pwd Get User
 * @apiName CheckUser
 * @apiGroup User
 *
 * @apiParam {String} userName UserName that wants to login in the system.
 * @apiParam {String} pwd Password of the user that wants to login in the system.
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *   events: [
 *     {
 *       cal: {
 *         year: Number,
 *         month: Number,
 *         dayOfMonth: Number,
 *         hourOfDay: Number,
 *         minute: Number,
 *         second: Number
 *       },
 *       day: Number,
 *       duration: Number,
 *       id: 1,
 *       importance: Number,
 *       month: Number,
 *       title: String,
 *       year: Number
 *     }
 *   ],
 *   friends: [
 *     {
 *       id: Number,
 *       idUserFrom: String,
 *       idUserTo: String,
 *       state: String
 *     },
 *     {
 *       id: Number,
 *       idUserFrom: String,
 *       idUserTo: String,
 *       state: String
 *     }
 *   ],
 *   idUser: Number,
 *   mail: String,
 *   name: String,
 *   password: String,
 *   surname: String,
 *   userName: String
 * }
 * 
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * 
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 401 Unauthorized
 * @apiVersion 1.0.0
 *
 * */
exports.findById = function (req, res) {
    console.log('GET /userId/' + req.params.userName + '/' + req.params.pwd);
    modelUser.findOne({ "userName": req.params.userName, "password": req.params.pwd }, function (err, userDB) {
        if (err) return res.send(500, err.message);
        res.status(200).jsonp(userDB);
    });
};

/**
 * @api {post} /users/:userName Exists User
 * @apiName ExistsUser
 * @apiGroup User
 * 
 * @apiParam {String} userName UserName that wants to check if exists
 * 
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     True
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     False
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiVersion 1.0.0
 * */
exports.checkUser = function (req, res) {
    console.log('GET /checkUser/' + req.params.userName);
    modelUser.findOne({ "userName": req.params.userName }, function (err, userDB) {
        if (err) return res.send(500, err.message);
        if (userDB) {
            res.status(200).send("true");
        } else {
            res.status(200).send("false");
        }
    });
};

/**
 * @api {put} /users Update user
 * @apiName UpdateUser
 * @apiGroup User
 * 
 * @apiParamExample {json} Request-Example:
 * {
 *   "events": [
 *     {
 *       "cal": {
 *         "year": 2018,
 *         "month": 4,
 *         "dayOfMonth": 9,
 *         "hourOfDay": 0,
 *         "minute": 0,
 *         "second": 0
 *       }
 *   ],
 *   "friends": [
 *     {
 *       "id": 30,
 *       "idUserFrom": "testUserFrom",
 *       "idUserTo": "testUserTo",
 *       "state": "friend"
 *     }
 *   ],
 *   "idUser": 123,
 *   "mail": "test@mail.com",
 *   "name": "test",
 *   "password": "test",
 *   "surname": "test",
 *   "userName": "test"
 * }
 * 
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *        result: "Updated"
 *     }
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiVersion 1.0.0
 * */
exports.updateUser = function (req, res) {
    console.log("PUT /users");
    modelUser.findOne({ "userName": req.body.userName, "password": req.body.password }, function (err, userBBDD) {
        if (userBBDD === null || userBBDD === undefined)
            return res.send(401, "Unauthorized");

        userBBDD.events = req.body.events;
        userBBDD.friends = req.body.friends;
        userBBDD.idUser = req.body.idUser;
        userBBDD.mail = req.body.mail;
        userBBDD.name = req.body.name;
        userBBDD.password = req.body.password;
        userBBDD.surname = req.body.surname;
        userBBDD.userName = req.body.userName;
        userBBDD.lastUpdate = req.body.lastUpdate

        userBBDD.save(function (err) {
            if (err) return res.send(500, err.message);
            res.status(200).jsonp(userBBDD);
        });
    });
};

/**
 * @api {post} /friend/:userName/:pwd Add friend
 * @apiName AddFriend
 * @apiGroup Friends
 * 
 * @apiParam {String} userName UserName that wants to login in the system.
 * @apiParam {String} pwd Password of the user that wants to login in the system.
 * @apiParamExample {json} Request-Example:
 *{
 *   "id": 30,
 *   "idUserFrom": "testUserFrom",
 *   "idUserTo": "testUserTo",
 *   "state": "friend"
 * }
 * 
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *        result: "Do it"
 *     }
 * 
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 405 Bad request
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 406 User from dont exists
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 407 User to dont exists
 * @apiVersion 1.0.0
 * */
exports.addFriend = function (req, res) {
    console.log("/friend POST");
    userFrom = modelUser.findOne({ "userName": req.params.userName, "password": req.params.pwd }, function (err, userFrom) {
        if (err) {
            return res.send(500, err.message);
        }
        userTo = modelUser.findOne({ "userName": req.body.idUserTo }, function (err, userTo) {
            if (err) {
                return res.send(500, err.message);
            }
            if (req.params.userName != req.body.idUserFrom) {
                return res.send(405, "Bad request");
            }
            else if (userFrom === null || userFrom === undefined) {
                return res.send(406, "User from dont exists");
            } else if (userTo === null || userTo === undefined) {
                return res.send(407, "User to dont exists");
            } else {
                modelUser.find({
                    $or: [
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserFrom: req.body.idUserTo } } },
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserTo: req.body.idUserTo } } }]
                }, function (err, users) {
                    if (users[0] === null || users[0] === undefined) {
                        userFrom.friends = userFrom.friends.concat(req.body);
                        userTo.friends = userTo.friends.concat(req.body);
                        userFrom.save(function (err) {
                            if (err) {
                                return res.send(500, err.message);
                            }
                            userTo.save(function (err) {
                                if (err) {
                                    return res.send(500, err.message);
                                }
                                res.status(200).jsonp({ result: "Do it" });
                            });
                        });
                    } else {
                        res.send(405, "You already have it that friend");
                    }
                });
            }
        });
    });
};

/**
 * @api {delete} /friend/:userName/:pwd Delete friend
 * @apiName DeleteFriend
 * @apiGroup Friends
 * 
 * @apiParam {String} userName UserName that wants to login in the system.
 * @apiParam {String} pwd Password of the user that wants to login in the system.
 * @apiParamExample {json} Request-Example:
 *{
 *   "id": 30,
 *   "idUserFrom": "testUserFrom",
 *   "idUserTo": "testUserTo",
 *   "state": "friend"
 * }
 * 
 * @apiSuccessExample Success-Response:
 * HTTP/1.1 200 OK
 * {
 *   result: "Do it"
 * }
 * 
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 405 Bad request
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 406 User from dont exists
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 407 User to dont exists
 * @apiVersion 1.0.0
 * */
exports.deleteFriend = function (req, res) {
    userFrom = modelUser.findOne({ "userName": req.params.userName, "password": req.params.pwd }, function (err, userFrom) {
        if (err) {
            return res.send(500, err.message);
        }
        userTo = modelUser.findOne({ "userName": req.body.idUserTo }, function (err, userTo) {
            if (err) {
                return res.send(500, err.message);
            }
            if (req.params.userName != req.body.idUserFrom) {
                return res.send(405, "Bad request");
            }
            else if (userFrom === null || userFrom === undefined) {
                return res.send(406, "User from dont exists");
            } else if (userTo === null || userTo === undefined) {
                return res.send(407, "User to dont exists");
            } else {
                modelUser.find({
                    $or: [
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserFrom: req.body.idUserTo } } },
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserTo: req.body.idUserTo } } }]
                }, function (err, users) {
                    if (users[0] === null || users[0] === undefined) {
                        res.send(405, "You dont have this friend");
                    } else {
                        modelUser.update({ "userName": req.body.idUserFrom },
                            { $pull: { friends: { "idUserFrom": req.body.idUserTo } } },
                            { safe: true, upsert: true }).exec();
                        modelUser.update({ "userName": req.body.idUserFrom },
                            { $pull: { friends: { "idUserTo": req.body.idUserTo } } },
                            { safe: true, upsert: true }).exec();

                        modelUser.update({ "userName": req.body.idUserTo },
                            { $pull: { friends: { "idUserFrom": req.body.idUserFrom } } },
                            { safe: true, upsert: true }).exec();
                        modelUser.update({ "userName": req.body.idUserTo },
                            { $pull: { friends: { "idUserTo": req.body.idUserFrom } } },
                            { safe: true, upsert: true }).exec();

                        res.status(200).jsonp({ result: "deleted" });
                    }
                });
            }
        });
    });
};

/**
 * @api {put} /friend/:userName/:pwd Update friend
 * @apiName UpdateFriend
 * @apiGroup Friends
 * 
 * @apiParam {String} userName UserName that wants to login in the system.
 * @apiParam {String} pwd Password of the user that wants to login in the system.
 * @apiParamExample {json} Request-Example:
 *{
 *   "id": 30,
 *   "idUserFrom": "testUserFrom",
 *   "idUserTo": "testUserTo",
 *   "state": "friend"
 * }
 * 
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *        result: "Update it"
 *     }
 * 
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 500 Internal System Error
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 405 Bad request
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 406 User from dont exists
 * @apiErrorExample Error-Response:
 *     HTTP/1.1 407 User to dont exists
 * @apiVersion 1.0.0
 * */
exports.updateFriend = function (req, res) {
    userFrom = modelUser.findOne({ "userName": req.params.userName, "password": req.params.pwd }, function (err, userFrom) {
        if (err) {
            return res.send(500, err.message);
        }
        userTo = modelUser.findOne({ "userName": req.body.idUserTo }, function (err, userTo) {
            if (err) {
                return res.send(500, err.message);
            }
            if (req.params.userName != req.body.idUserFrom) {
                return res.send(405, "Bad request");
            }
            else if (userFrom === null || userFrom === undefined) {
                return res.send(406, "User from dont exists");
            } else if (userTo === null || userTo === undefined) {
                return res.send(407, "User to dont exists");
            } else {
                modelUser.find({
                    $or: [
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserFrom: req.body.idUserTo } } },
                        { "userName": req.body.idUserFrom, friends: { $elemMatch: { idUserTo: req.body.idUserTo } } }]
                }, function (err, users) {
                    if (users[0] === null || users[0] === undefined) {
                        res.send(405, "You dont have this friend");
                    } else {
                        modelUser.update({ "userName": req.body.idUserFrom, "friends.idUserTo": req.body.idUserTo },
                            { $set: { "friends.$.state": req.body.state } },
                            false, true).exec();

                        modelUser.update({ "userName": req.body.idUserTo, "friends.idUserTo": req.body.idUserTo },
                            { $set: { "friends.$.state": req.body.state } },
                            false, true).exec();

                        res.status(200).jsonp({ result: "Update it" });
                    }
                });
            }
        });
    });
};
