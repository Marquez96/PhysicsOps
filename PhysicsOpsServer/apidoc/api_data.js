define({ "api": [
  {
    "type": "post",
    "url": "/friend/:userName/:pwd",
    "title": "Add friend",
    "name": "AddFriend",
    "group": "Friends",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>UserName that wants to login in the system.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pwd",
            "description": "<p>Password of the user that wants to login in the system.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n  \"id\": 30,\n  \"idUserFrom\": \"testUserFrom\",\n  \"idUserTo\": \"testUserTo\",\n  \"state\": \"friend\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   result: \"Do it\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 405 Bad request",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 406 User from dont exists",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 407 User to dont exists",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "Friends"
  },
  {
    "type": "delete",
    "url": "/friend/:userName/:pwd",
    "title": "Delete friend",
    "name": "DeleteFriend",
    "group": "Friends",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>UserName that wants to login in the system.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pwd",
            "description": "<p>Password of the user that wants to login in the system.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n  \"id\": 30,\n  \"idUserFrom\": \"testUserFrom\",\n  \"idUserTo\": \"testUserTo\",\n  \"state\": \"friend\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n  result: \"Do it\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 405 Bad request",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 406 User from dont exists",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 407 User to dont exists",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "Friends"
  },
  {
    "type": "put",
    "url": "/friend/:userName/:pwd",
    "title": "Update friend",
    "name": "UpdateFriend",
    "group": "Friends",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>UserName that wants to login in the system.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pwd",
            "description": "<p>Password of the user that wants to login in the system.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n  \"id\": 30,\n  \"idUserFrom\": \"testUserFrom\",\n  \"idUserTo\": \"testUserTo\",\n  \"state\": \"friend\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   result: \"Update it\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 405 Bad request",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 406 User from dont exists",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 407 User to dont exists",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "Friends"
  },
  {
    "type": "get",
    "url": "/user/:username/:pwd",
    "title": "Get User",
    "name": "CheckUser",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>UserName that wants to login in the system.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pwd",
            "description": "<p>Password of the user that wants to login in the system.</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n  events: [\n    {\n      cal: {\n        year: Number,\n        month: Number,\n        dayOfMonth: Number,\n        hourOfDay: Number,\n        minute: Number,\n        second: Number\n      },\n      day: Number,\n      duration: Number,\n      id: 1,\n      importance: Number,\n      month: Number,\n      title: String,\n      year: Number\n    }\n  ],\n  friends: [\n    {\n      id: Number,\n      idUserFrom: String,\n      idUserTo: String,\n      state: String\n    },\n    {\n      id: Number,\n      idUserFrom: String,\n      idUserTo: String,\n      state: String\n    }\n  ],\n  idUser: Number,\n  mail: String,\n  name: String,\n  password: String,\n  surname: String,\n  userName: String\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        },
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 401 Unauthorized",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "User"
  },
  {
    "type": "post",
    "url": "/users/:userName",
    "title": "Exists User",
    "name": "ExistsUser",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "userName",
            "description": "<p>UserName that wants to check if exists</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\nTrue",
          "type": "json"
        },
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\nFalse",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "User"
  },
  {
    "type": "post",
    "url": "/user/:id",
    "title": "Insert user",
    "name": "InsertUser",
    "group": "User",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n  \"events\": [\n    {\n      \"cal\": {\n        \"year\": 2018,\n        \"month\": 4,\n        \"dayOfMonth\": 9,\n        \"hourOfDay\": 0,\n        \"minute\": 0,\n        \"second\": 0\n      },\n      \"day\": 9,\n      \"duration\": 1,\n      \"id\": 1,\n      \"importance\": 1,\n      \"month\": 4,\n      \"title\": \"test\",\n      \"year\": 2018\n    }\n  ],\n  \"friends\": [\n    {\n      \"id\": 30,\n      \"idUserFrom\": \"testUserFrom\",\n      \"idUserTo\": \"testUserTo\",\n      \"state\": \"friend\"\n    },\n    {\n      \"id\": 34,\n      \"idUserFrom\": \"admin\",\n      \"idUserTo\": \"carlos\",\n      \"state\": \"friend\"\n    }\n  ],\n  \"idUser\": 123,\n  \"mail\": \"test@mail.com\",\n  \"name\": \"test\",\n  \"password\": \"test\",\n  \"surname\": \"test\",\n  \"userName\": \"test\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   result: \"Inserted\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "User"
  },
  {
    "type": "put",
    "url": "/users",
    "title": "Update user",
    "name": "UpdateUser",
    "group": "User",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n  \"events\": [\n    {\n      \"cal\": {\n        \"year\": 2018,\n        \"month\": 4,\n        \"dayOfMonth\": 9,\n        \"hourOfDay\": 0,\n        \"minute\": 0,\n        \"second\": 0\n      }\n  ],\n  \"friends\": [\n    {\n      \"id\": 30,\n      \"idUserFrom\": \"testUserFrom\",\n      \"idUserTo\": \"testUserTo\",\n      \"state\": \"friend\"\n    }\n  ],\n  \"idUser\": 123,\n  \"mail\": \"test@mail.com\",\n  \"name\": \"test\",\n  \"password\": \"test\",\n  \"surname\": \"test\",\n  \"userName\": \"test\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   result: \"Updated\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "Error-Response:",
          "content": "HTTP/1.1 500 Internal System Error",
          "type": "json"
        }
      ]
    },
    "version": "1.0.0",
    "filename": "controllers/users.js",
    "groupTitle": "User"
  }
] });
