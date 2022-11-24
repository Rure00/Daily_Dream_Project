//import JSONObject from org.json.simple;
//import JSONArray from org.json.simple;
//import JSONParser from org.json.simple.parser;
//import ParseException from org.json.simple.parser;


var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.listen(3000, '0.0.0.0', function () {
    console.log('서버 실행 중...');
});

var userDB_connection = mysql.createConnection({
    host: "13.209.201.89",
    user: "admin",
    database: "User",
    password: "24456344",
    port: 3306
});

var postDB_connection = mysql.createConnection({
    host: "13.209.201.89",
    user: "admin",
    database: "Post",
    password: "24456344",
    port: 3306
})

app.post('/user/join', function (req, res) {
    var userName = req.body.UserName;
    var loginID = req.body.LoginID;
    var loginPwd = req.body.LoginPwd;
    var division = req.body.Division;
    var department = req.body.Department;
    var belong = req.body.Belong;
    

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO users (Name, LoginID, LoginPwd, Division, Department, Belong) VALUES (?, ?, ?, ?, ?, ?)';
    var params = [userName, loginID, loginPwd, division, department, belong];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    userDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if(result.length == 0) {
                resultCode = 204;
                message = '이미 가입한 계정이 있습니다.';
            }
            else {
                resultCode = 200;
                console.log(userName + "님이 회원가입했습니다.")
                message = '회원가입에 성공했습니다.';
            }
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

app.post('/user/login', function (req, res) {
    var loginID = req.body.LoginID;
    var loginPwd = req.body.LoginPwd;
    var sql = 'select * from users where LoginID = ?';

    userDB_connection.query(sql, loginID, function(err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else if (loginPwd !== result[0].LoginPwd) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].Name + '님 환영합니다!';
            }
        }

        if(resultCode == 200) 
        {
            res.json({
                'code': resultCode,
                'message': message,
                'UserName': result[0].Name,
                'ID': result[0].ID,
                'Department' : result[0].Department,
                'Division' : result[0].Division,
                'Belong' : result[0].Belong
            });

            console.log(result[0].Name + "님이 로그인하셨습니다.");
        }
        else {
            res.json({
                'code': resultCode,
                'message': message
            });
        }
        
    })
});

app.post('/post/posting', function (req, res) {
    var title = req.body.Title;
    var content = req.body.Content;
    var userName = req.body.UserName;
    var userID = req.body.UserID;
    var typeOfPost = req.body.TypeOfPost;

    var sql = 'INSERT INTO posts (userName, userID, Title, Content, typeOfPost) VALUES (?, ?, ?, ?, ?)';
    var params = [userName, userID, title, content, typeOfPost];

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message ="포스트가 성공적으로 업로드 되었습니다.";
            console.log(userName + "님이 글을 올렸습니다.")
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});

app.post('/post/commenting', function (req, res) {
    var content = req.body.Content;
    var userName = req.body.userName;
    var postId = req.body.postID;
    var userId = req.body.userID;

    var sql = 'INSERT INTO Comments (userID, Content, postID, UserName) VALUES (?, ?, ?, ?)';
    var params = [userId, content, postId, userName];

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message ="포스트가 성공적으로 업로드 되었습니다.";
            console.log(userName + "님이 댓글을 작성했습니다.")
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});

app.get('/post/getPosts', function(req, res) {
    var lastID = req.query.ID;
    var type = req.query.typeOfPost;
    var postNum = 20

    var sql = '';
    var params = [];

    if(lastID == 0) {
        sql = 'SELECT * FROM posts Where typeOfPost = ? ORDER BY Creation_Date DESC LIMIT ?';
        params = [type, postNum];
    } else {
        sql = 'SELECT * FROM posts Where typeOfPost = ? AND ID < ? ORDER BY Creation_Date DESC LIMIT ?';
        params = [type, lastID, postNum];
    }

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if(result.length == 0) {
                resultcode = 202;
                message = "결과가 없습니다.";
            }
            else {
                resultCode = 200;
                message ="포스트를 로드했습니다.";
            }
        } 

        if(resultCode == 200) {
            var isSuccessful = false;
            var resultArr = new Array();

            for (var i = 0; i< postNum && i < result.length; i++) {
                var jsonStr = JSON.stringify(result[i]);
                resultArr.push(jsonStr);
            }

            if(result.length == postNum) {
                isSuccessful = true;
            } else if(result[result.length - 1].ID == 1) {
                isSuccessful = true;
            }

            res.json({
                'code' : resultCode,
                'message' : message,
                'isSuccessful': isSuccessful,
                'ResultArr': resultArr
            })

            return;
        }

        res.json({
            'code': resultCode,
            'isSuccessful': isSuccessful,
            'message': message
        });
    })
});

app.get('/post/getPost', function(req, res) {
    var postID = req.query.ID;

    var sql = 'SELECT * FROM posts Where ID = ?';
    var params = [postID];

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if(result.length == 0) {
                resultcode = 202;
                message = "결과가 없습니다.";
            }
            else {
                resultCode = 200;
                message ="포스트를 로드했습니다.";
            }
        } 

        if(resultCode == 200) {

            var isSuccessful = false;

            isSuccessful = true;

            res.json({
                'code' : resultCode,
                'message' : message,
                'isSuccessful': isSuccessful,
                'Title': result[0].Title,
                'Content': result[0].Content,
                'UserName': result[0].userName,
                'PostID': result[0].ID,
            })

            return;
        }

        res.json({
            'code': resultCode,
            'isSuccessful': isSuccessful,
            'message': message
        });
    })
});

app.get('/post/getComments', function(req, res) {
    var postID = req.query.ID;

    var sql = 'SELECT * FROM Comments Where postID = ? ORDER BY Creation_Date ASC';
    var params = [postID];

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if(result.length == 0) {
                resultcode = 202;
                message = "결과가 없습니다.";
            }
            else {
                resultCode = 200;
                message ="포스트를 로드했습니다.";
            }
        } 

        if(resultCode == 200) {
            var isSuccessful = true;
            var resultArr = new Array();

            for (var i = 0; i < result.length; i++) {
                var jsonStr = JSON.stringify(result[i]);
                resultArr.push(jsonStr);
            }

            res.json({
                'code' : resultCode,
                'message' : message,
                'isSuccessful': isSuccessful,
                'ResultArr': resultArr
            })

            return;
        }

        res.json({
            'code': resultCode,
            'isSuccessful': isSuccessful,
            'message': message
        });
    })
});

app.get('/post/searchPosts', function(req, res) {
    var searchWord = "%" + req.query.SearchWord + "%";
    var typeOfPost = req.query.typeOfPost;
    var lastID = req.query.Id;
    var postNum = 20

    var sql= '';
    var params = [];

    if(lastID == 0) {
        sql = 'SELECT * FROM posts Where typeOfPost = ? AND (Title LIKE ? OR Content LIKE ?)  ORDER BY Creation_Date DESC LIMIT ?';
        params = [typeOfPost, searchWord, searchWord, postNum];
    } else {
        sql = 'SELECT * FROM posts Where typeOfPost = ? AND ID < ? AND (Title LIKE ? OR Content LIKE ?) ORDER BY Creation_Date DESC LIMIT ?';
        params = [typeOfPost, lastID, searchWord, searchWord, postNum];
    }

    postDB_connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if(result.length == 0) {
                resultcode = 202;
                message = "결과가 없습니다.";
            }
            else {
                resultCode = 200;
                message ="포스트를 로드했습니다.";
            }
        } 

        if(resultCode == 200) {
            var isSuccessful = true;
            var resultArr = new Array();

            for (var i = 0; i< postNum && i < result.length; i++) {
                var jsonStr = JSON.stringify(result[i]);
                resultArr.push(jsonStr);
            }

            res.json({
                'code' : resultCode,
                'message' : message,
                'isSuccessful': isSuccessful,
                'ResultArr': resultArr
            })

            return;
        }

        res.json({
            'code': resultCode,
            'isSuccessful': isSuccessful,
            'message': message
        });
    })
});
