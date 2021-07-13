const express= require('express');
var bodyParser = require('body-parser');
const Datastore = require('nedb');
const app= express();
var port=process.env.PORT || 3000;
app.listen(port,()=>console.log("listening at 3000..."));
app.use(express.static('public'));
app.use(express.json({ limit: '1mb' }));

var data;
const database = new Datastore('database.db');
database.loadDatabase();

app.post('/api', (request, response) => {
   data = request.body;
  console.log(data);

  const timestamp = Date.now();
  data.timestamp = timestamp;

  database.insert(data);
  console.log(database);
  response.json(data);
});
app.post('/path',(request,response)=>{
	console.log("client connected");
     const idp=request.body;
     var x=parseFloat(idp.id);
      console.log(x);
     var db =null;
     database.find({ id: x }).sort({ timestamp: 1 }).exec(function (err, docs) {
db=docs ;
do_something_when_you_get_your_result();
});


     function do_something_when_you_get_your_result() {
       console.log(db);
       response.json(db);
     }
});
app.use(bodyParser.json());
app.post('/mob', function(req, res) {
console.log("client connected");
console.log(req.body);
 const idp=req.body;
 //console.log(idp);
     var x=parseFloat(idp.id);
   var db =null;
     database.find({id: x}).sort({ timestamp: 1 }).exec(function (err, docs) {
db=docs ;
do_something_when_you_get_your_result();
});


     function do_something_when_you_get_your_result() {
       console.log(db);
       res.json(db);
     }
});

app.post('/historic',(request,response)=>{
  console.log("client connected");
     const idp=request.body;
     var x=parseFloat(idp.id);
      console.log(x);
     var db =null;
     database.find({ id: x }).sort({ timestamp: 1 }).exec(function (err, docs) {
db=docs ;
do_something_when_you_get_your_result();
});


     function do_something_when_you_get_your_result() {
       console.log(db);
       response.json(db);
     }
});
