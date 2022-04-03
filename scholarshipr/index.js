// set up Express
var express = require('express');
var app = express();

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// import the scholarship class from Scholarship.js
var Scholarship = require('./Scholarship.js');

/***************************************/

// endpoint for creating a new 
// this is the action of the "create new scholarship" form
app.use('/create', (req, res) => {
	// construct the scholarship from the form data which is in the request body
	var newScholarship = new Scholarship ({
		name: req.body.name,
		org: req.body.org,
		description: req.body.description,
		dollarAmount: req.body.dollarAmount,
		approvalStatus: req.body.approvalStatus,
		dueDate: req.body.dueDate,
		gpaRequirement: req.body.gpaRequirement,
	    });

	// save the scholarship to the database
	newScholarship.save( (err) => { 
		if (err) {
		    res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" message
		    res.send('successfully added ' + newScholarship.name + ' to the database');
		}
	    } ); 
    }
    );

// endpoint for showing all the scholarships
app.use('/all', (req, res) => {

	// res.redirect('/public/tableOfData.html');

	// /*


	// find all the scholarship objects in the database
	Scholarship.find( {}, (err, scholarships) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		}
		else {
		    if (scholarships.length == 0) {
			res.type('html').status(200);
			res.write('There are no scholarships');
			res.end();
			return;
		    }
		    else {
			res.type('html').status(200);
			res.write('Here are the scholarships in the database:');
			res.write('<ul>');
			// show all the scholarships
			scholarships.forEach( (scholarship) => {
			    res.write('<li>');
			    res.write('Name: ' + scholarship.name + '; Org: ' + scholarship.org + '; $ Amount: ' + scholarship.dollarAmount + '; Due Date: ' + scholarship.dueDate);
			    // this creates a link to the /delete endpoint
			    res.write(" <a href=\"/delete?name=" + scholarship.name + "\">[Delete]</a>");
			    res.write('</li>');
					 
			});
			res.write('</ul>');
			res.end();
		    }
		}
	    }).sort({ 'age': 'asc' }); // this sorts them BEFORE rendering the results

	// */

	});


// IMPLEMENT THIS ENDPOINT!
app.use('/delete', (req, res) => {
    res.redirect('/all');
});

// endpoint for accessing data via the web api
// to use this, make a request for /api to get an array of all Scholarship objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {

	// construct the query object
	var queryObject = {};
	if (req.query.name) {
	    // if there's a name in the query parameter, use it here
	    queryObject = { "name" : req.query.name };
	}
    
	Scholarship.find( queryObject, (err, scholarships) => {
		console.log(scholarships);
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		}
		else if (scholarships.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		}
		else if (scholarships.length == 1 ) {
		    var scholarship = scholarships[0];
		    // send back a single JSON object
		    res.json( { "name" : scholarship.name , "age" : scholarship.age } );
		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    scholarships.forEach( (scholarship) => {
			    returnArray.push( { "name" : scholarship.name, "age" : scholarship.age } );
			});
		    // send it back as JSON Array
		    res.json(returnArray); 
		}
		
	    });
    });




/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/scholarshipform.html'); } );

app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
