// set up Express
var express = require('express');
var app = express();
const url = require('url');    


app.set('view engine', 'ejs');
app.set('views', './public')
// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// import the scholarship class from Scholarship.js
var Scholarship = require('./Scholarship.js');

/***************************************/

// endpoint for creating a new 
// this is the action of the "create new scholarship" form
app.use('/create', (req, res) => {

	if (!req.body.name ) {
		res.redirect('/');
	}

	// construct the scholarship from the form data which is in the request body
	var newScholarship = new Scholarship({
		name: req.body.name,
		org: req.body.org,
		description: req.body.description,
		dollarAmount: req.body.dollarAmount,
		approvalStatus: (req.body.approvalStatus == "1" ? true : false),
		dueDate: req.body.dueDate,
		gpaRequirement: req.body.gpaRequirement,
	});

	// save the scholarship to the database
	newScholarship.save((err) => {
		if (err) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		}
		else {
			// display the "successfull created" message
			res.type('html').status(200);
			res.write('successfully added ' + newScholarship.name + ' to the database');
			res.write("<a href=\"/viewDetail?_id=" + newScholarship._id + "\">[See Completed Entry]</a>")
			res.end();
		}
	});
}
);


app.use('/edit', (req, res) => {
	var query = {"_id" : req.query._id };
	Scholarship.findOne( query, (err, result) => {
		if (err) {
		    res.render("error", {'error' : err});
		}
		else {
		    // this uses EJS to render the views/editForm.ejs template	
		    res.render("edit", {"scholarship" : result});
		}
	    });
    });

// app.use('/edit', (req, res) => {
// 	res.redirect('/public/edit.html', { _id: req.query._id });
// });
// endpoint for editing an existing scholarship
// this is the action of the "edit scholarship" form


app.use('/update', (req, res) => {
	// construct the scholarship from the form data which is in the request body
	var filter = { '_id': req.query._id };
	var action = {
		'$set': {
			name: (req.query.name ? req.query.name : req.body.name),
			org: (req.query.org ? req.query.org : req.body.org),
			description: (req.query.description ? req.query.description : req.body.description),
			dollarAmount: (req.query.dollarAmount ? req.query.dollarAmount : req.body.dollarAmount),
			approvalStatus: (req.query.approvalStatus ? req.query.approvalStatus : req.body.approvalStatus),
			dueDate: (req.query.dueDate ? req.query.dueDate : req.body.dueDate),
			gpaRequirement: (req.query.gpaRequirement ? req.query.gpaRequirement : req.body.gpaRequirement),
		}
	};


	Scholarship.findOneAndUpdate(filter, action, (err, orig) => {
		if (err) {
			res.json({ 'status': err });
		}
		else if (!orig) {
			res.json({ 'status': 'scholarship not found' });
		}
		else {
			res.json({ 'status': 'success' });
		}
	});

}
);

// endpoint for showing all the scholarships
app.use('/all', (req, res) => {

	// res.redirect('/public/tableOfData.html');
	res.type('html').status(200);

	// starterTemplate = '<htmllang=\"en\">	<head>	<!--Requiredmetatags-->	<metacharset=\"utf-8\">	<metaname=\"viewport\"content=\"width=device-width,initial-scale=1,shrink-to-fit=no\">	<!--BootstrapCSS-->	<linkrel=\"stylesheet\"href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\"integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"crossorigin=\"anonymous\">	<title>Hello,world!</title>	</head>	<body>'

	res.write("<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">    <a class=\"navbar-brand\" href=\"/\">Scholarshipr</a>    <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNavAltMarkup\" aria-controls=\"navbarNavAltMarkup\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">      <span class=\"navbar-toggler-icon\"></span>    </button>    <div class=\"collapse navbar-collapse\" id=\"navbarNavAltMarkup\">      <div class=\"navbar-nav\">        <a class=\"nav-item nav-link\" href=\"/all\">All</a>        <a class=\"nav-item nav-link\" href=\"/public/create.html\">Add</a>        <a class=\"nav-item nav-link\" href=\"#\">Suggested</a>      </div>    </div>  </nav>")
	res.write("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");
	// res.write(starterTemplate)

	// find all the scholarship objects in the database
	Scholarship.find({}, (err, scholarships) => {
		if (err) {
			console.log('uh oh' + err);
			res.write(err);
		}
		else {
			if (scholarships.length == 0) {
				res.write('There are no scholarships');
				res.end();
				// return;
			}
			else {
				res.write('<h3>Here are the scholarships in the database:</h3>');
				res.write('<table class="table table-light">');

				//Establish the table header
				var header = "<tr><th>Id</th><th>Name</th><th>Organization</th><th>Dollar Amount</th><th>Due Date</th></tr>";
				res.write(header);

				// show all the scholarships
				scholarships.forEach((scholarship) => {

					res.write('<tr>');

					res.write('<td>' + scholarship._id + '</td>')
					res.write('<td>' + scholarship.name + '</td>')
					res.write('<td>' + scholarship.org + '</td>')
					res.write('<td>' + scholarship.dollarAmount + '</td>')
					if (scholarship.dueDate) {
						res.write('<td>' + scholarship.dueDate.toDateString() + '</td>')
					} else {
						res.write('<td>' + 'Unknown' + '</td>')
					}
					res.write("<td>" +
						" <a class=\"btn btn-danger btn-sm\" href=\"/delete?_id=" + scholarship._id + "\">Delete</a>" +
						"</td>"
					)
					res.write("<td>" +
						" <a class=\"btn btn-warning btn-sm\" href=\"/edit?_id=" + scholarship._id + "\">Edit</a>" +
						"</td>"
					)
					res.write("<td>" +
					" <a class=\"btn btn-info btn-sm\" href=\"/viewDetail?_id=" + scholarship._id + "\">ViewDetail</a>" +
					"</td>"
					)

					res.write('</tr>');

				});
				res.write('</table>');
				res.end();
			}
		}
	}).sort({ 'dollarAmount': 'desc' }); // this sorts them BEFORE rendering the results

	res.write('  </body></html>')

});

app.use('/viewDetail', (req, res) => {
	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.type('html').write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to find scholarship: ${id}`)
		Scholarship.findOne({'_id':id}, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.type('html').write("Unexpected error")
			}
			else if (!scholarship) {
				// A strange error I can't debug happens here. Recreate by
				// visiting http://localhost:3000/delete?_id=5
				console.log(`Could not find scholarship with id ${id}`)
				return res.type('html').write(`Could not find scholarship with id ${id}`)
			}
			else {
				console.log(`Found scholarship with id ${scholarship._id}`)
				res.type('html').status(200);

				// starterTemplate = '<htmllang=\"en\">	<head>	<!--Requiredmetatags-->	<metacharset=\"utf-8\">	<metaname=\"viewport\"content=\"width=device-width,initial-scale=1,shrink-to-fit=no\">	<!--BootstrapCSS-->	<linkrel=\"stylesheet\"href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\"integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\"crossorigin=\"anonymous\">	<title>Hello,world!</title>	</head>	<body>'
			
				res.write("<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">    <a class=\"navbar-brand\" href=\"/\">Scholarshipr</a>    <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNavAltMarkup\" aria-controls=\"navbarNavAltMarkup\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">      <span class=\"navbar-toggler-icon\"></span>    </button>    <div class=\"collapse navbar-collapse\" id=\"navbarNavAltMarkup\">      <div class=\"navbar-nav\">        <a class=\"nav-item nav-link\" href=\"/all\">All</a>        <a class=\"nav-item nav-link\" href=\"/public/create.html\">Add</a>        <a class=\"nav-item nav-link\" href=\"#\">Suggested</a>      </div>    </div>  </nav>")
				res.write("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");			
				// res.write('<h3>Here is a specific scholarship</h3>');

				res.write(getCardHTML(scholarship));
			}
		});
	}
 });

function getCardHTML(scholarship) {

	let name = scholarship.name;
	let description = scholarship.description;
	let org = scholarship.org;
	let dollarAmount = scholarship.dollarAmount;
	let approvalStatus = scholarship.approvalStatus;
	let dueDate = scholarship.dueDate;
	let gpaRequirement = scholarship.gpaRequirement;

	approvalEmoji = "❓"
	if (approvalStatus == true) {
		approvalEmoji = "✅";
	} else if (approvalStatus == false) {
		approvalStatus = "❌";
	}

	let returnableString = "<div class=\"col d-flex justify-content-center\">";
	returnableString += "<div class=\"card\" style=\"width: 18rem;\"> <div class=\"card-body\">    <h5 class=\"card-title\">";
	returnableString += name;
	returnableString += "</h5>    <p class=\"card-text\">";
	returnableString += description;
	returnableString += "</p>  </div>  <ul class=\"list-group list-group-flush\">    <li class=\"list-group-item\">";
	returnableString += "Org: " + org;
	returnableString += "</li>    <li class=\"list-group-item\">";
	returnableString += "Dollar Amount: " + dollarAmount;
	returnableString += "</li>    <li class=\"list-group-item\">";
	returnableString += "Approval Status: " + approvalEmoji;
	returnableString += "</li>    <li class=\"list-group-item\">";

	if (scholarship.dueDate) {
		returnableString += "Due Date: " + dueDate.toDateString();
	} else {
		returnableString += "Due Date: " + "unknown";
	}

	returnableString += "</li>    <li class=\"list-group-item\">";
	returnableString += "GPA Requirement: " + gpaRequirement;
	returnableString += "</li>    <li class=\"list-group-item\">";

	returnableString += "</li>  </ul>  <div class=\"mx-auto\"><div class=\"card-body\">";
	returnableString += "<a class=\"btn btn-warning btn-sm mr-1\" href=\"/edit?_id=" + scholarship._id + "\">Edit</a>";
	// returnableString += "<br/>"
	returnableString += "<a class=\"btn btn-danger btn-sm ml-1\" href=\"/delete?_id=" + scholarship._id + "\">Delete</a>"; 
	returnableString += "</div></div></div></div>";

	return returnableString;
}

// IMPLEMENT THIS ENDPOINT!
app.use('/delete', (req, res) => {
	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.type('html').write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to delete scholarship: ${id}`)
		Scholarship.findOneAndDelete({'_id':id}, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.type('html').write("Unexpected error")
			}
			else if (!scholarship) {
				// A strange error I can't debug happens here. Recreate by
				// visiting http://localhost:3000/delete?_id=5
				console.log(`Could not find scholarship with id ${id}`)
				return res.type('html').write(`Could not find scholarship with id ${id}`)
			}
			else {
				console.log(`Deleted scholarship with id ${scholarship._id}`)
			}
		});
	}
    res.redirect(302, '/all');
});

// endpoint for accessing data via the web api
// to use this, make a request for /api to get an array of all Scholarship objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {

	// construct the query object
	var queryObject = {};
	if (req.query.name) {
		// if there's a name in the query parameter, use it here
		queryObject = { "name": req.query.name };
	}

	Scholarship.find(queryObject, (err, scholarships) => {
		console.log(scholarships);
		if (err) {
			console.log('uh oh' + err);
			res.json({});
		}
		else if (scholarships.length == 0) {
			// no objects found, so send back empty json
			res.json({});
		}
		else if (scholarships.length == 1) {
			var scholarship = scholarships[0];
			// send back a single JSON object
			res.json({ "name": scholarship.name, "age": scholarship.age });
		}
		else {
			// construct an array out of the result
			var returnArray = [];
			scholarships.forEach((scholarship) => {
				returnArray.push({ "name": scholarship.name, "age": scholarship.age });
			});
			// send it back as JSON Array
			res.json(returnArray);
		}

	});
});

/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/create.html'); });



app.listen(3000, () => {
	console.log('Listening on port 3000');
});
