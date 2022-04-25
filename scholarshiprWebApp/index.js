// set up Express
var express = require('express');
var app = express();
const url = require('url');    
let path = require('path');
let fs = require('fs');

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
	res.type('html').status(200);
	

	// construct the scholarship from the form data which is in the request body
	var newScholarship = new Scholarship({

		name: (req.query.name ? req.query.name : req.body.name),
		org: (req.query.org ? req.query.org : req.body.org),
		description: (req.query.description ? req.query.description : req.body.description),
		dollarAmount: (req.query.dollarAmount ? req.query.dollarAmount : req.body.dollarAmount),
		approvalStatus: (req.query.approvalStatus ? req.query.approvalStatus : (req.body.approvalStatus == "1" ? true : false)),
		dueDate: (req.query.dueDate ? req.query.dueDate : req.body.dueDate),
		gpaRequirement: (req.query.gpaRequirement ? req.query.gpaRequirement : req.body.gpaRequirement),
	});

	// save the scholarship to the database
	newScholarship.save((err) => {
		if (err) {
			console.log("errorrrr");
			console.log(err);

			res.write(getPageOutline());
			res.write("<div class=\"container mt-2 pl-2\">");
			res.write("<p>Sorry, there has been an error while creating a scholarship. Please see the details below:</p>");
			res.write("<p>" + err + "</p>");
			res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
			res.write(' </div> </body></html>');
			// res.end();
		}
		else {
			console.log("Successfully created new scholarship");
			res.redirect('/all');
		}
	});
}
);


app.use('/edit', (req, res) => {
	res.type('html').status(200);

	var query = {"_id" : req.query._id };
	Scholarship.findOne( query, (err, result) => {
		if (err) {
			res.write(getPageOutline());
			// res.write(starterTemplate)
			res.write("<div class=\"container mt-2 pl-2\">");
			res.write("<p>Sorry, there has been an error while editing a scholarship. Please see the details below:</p>");
			res.write("<p>" + err + "</p>");
			res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
			res.write(' </div> </body></html>');
			res.end();
		}
		else if (!result){
			res.write(getPageOutline());
			// res.write(starterTemplate)
			res.write("<div class=\"container mt-2 pl-2\">");
			res.write("<p>The scholarship with requested ID cannot be found.</p>");
			res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
			res.write(' </div> </body></html>');
			res.end();
		}
		else {
		    // this uses EJS to render the views/editForm.ejs template	
		    res.render("edit", {"scholarship" : result});
		}
	    });
    });

app.use('/update', (req, res) => {
	res.type('html').status(200);
	// construct the scholarship from the form data which is in the request body
	var filter = { '_id': req.query._id };
	
	var action = {
		'$set': {
			name: (req.query.name ? req.query.name : req.body.name),
			org: (req.query.org ? req.query.org : req.body.org),
			description: (req.query.description ? req.query.description : req.body.description),
			dollarAmount: (req.query.dollarAmount ? req.query.dollarAmount : req.body.dollarAmount),
			approvalStatus: (req.query.approvalStatus ? req.query.approvalStatus : (req.body.approvalStatus == "1" ? true : false)),
			dueDate: (req.query.dueDate ? req.query.dueDate : req.body.dueDate),
			gpaRequirement: (req.query.gpaRequirement ? req.query.gpaRequirement : req.body.gpaRequirement),
		}
	};


	Scholarship.findOneAndUpdate(filter, action, (err, orig) => {
		if (err) {
			
			res.write(getPageOutline());
			res.write("<div class=\"container mt-2 pl-2\">");
			res.write("<p>" + err + "</p>");
			res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
			res.write(' </div> </body></html>');
			res.end();
		}
		else if (!orig) {
			res.write(getPageOutline());
			res.write("<div class=\"container mt-2 pl-2\">");
			res.write("<p>Scholarship with requested ID was not found.</p>");
			res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
			res.write(' </div> </body></html>');
			res.end();
		}
		else {
			res.redirect('/all');
		}
	});

}
);

// endpoint for showing suggested scholarships
app.use('/suggested', (req, res) => {

	res.type('html').status(200);

	res.write(getPageOutline());
	res.write("<div class=\"container mt-2 pl-2\">");
	// find all the pending scholarship objects in the database
	Scholarship.find({approvalStatus: "false"}, (err, scholarships) => {
		if (err) {
			console.log('uh oh' + err);
			res.write(err);
		}
		else {
			if (scholarships.length == 0) {
				res.write('<p class="ml-2">There are no pending scholarships.</p>');
				res.end();
			}
			else {
				res.write('<h3>Here are the pending scholarships in the database:</h3>');
				res.write('<table class="table table-light">');

				//Establish the table header
				var header = "<tr><th>Id</th><th>Name</th><th>Organization</th><th>Dollar Amount</th><th>Due Date</th></tr>";
				res.write(header);

				// show all the scholarships
				scholarships.forEach((scholarship) => {

					res.write('<tr>');

					res.write('<td>' + scholarship._id + '</td>');
					res.write('<td>' + scholarship.name + '</td>');
					res.write('<td>' + scholarship.org + '</td>');
					res.write('<td>' + scholarship.dollarAmount + '</td>');
					if (scholarship.dueDate) {
						res.write('<td>' + scholarship.dueDate.toISOString().slice(0, 10) + '</td>');
					} else {
						res.write('<td>' + 'Unknown' + '</td>');
					}
					res.write('<td>' + 
						` <a class="btn btn-danger btn-sm" href="#" onclick="if (confirm('Delete scholarship &quot;` + scholarship.name + `&quot;?')) { window.location = '/delete?_id=` + scholarship.id + `' }">Delete</a>` +
						'</td>'
					);
					res.write("<td>" +
						' <a class="btn btn-warning btn-sm" href="/edit?_id=' + scholarship._id + '"\>Edit</a>' +
						'</td>'
					);
					res.write("<td>" +
					' <a class="btn btn-info btn-sm" href="/viewDetail?_id=' + scholarship._id + '">ViewDetail</a>' +
					'</td>'
					);
					res.write('<td>' + 
						` <a class="btn btn-success btn-sm" href="#" onclick="if (confirm('Approve scholarship &quot;` + scholarship.name + `&quot;?')) { window.location = '/approve?_id=` + scholarship.id + `' }">Approve</a>` +
						'</td>'
					);

					res.write('</tr>');

				});
				res.write('</table>');
				res.end();
			}
		}
	}).sort({ 'dollarAmount': 'desc' }); // this sorts them BEFORE rendering the results

	res.write(' </div> </body></html>');

});

// endpoint for showing all the scholarships
app.use('/all', (req, res) => {

	res.type('html').status(200);

	res.write(getPageOutline());
	res.write("<div class=\"container mt-2 pl-2\">");
	// find all the scholarship objects in the database with approvalStatus=="true"
	Scholarship.find({approvalStatus: "true"}, (err, scholarships) => {
		if (err) {
			console.log('uh oh' + err);
			res.write(err);
		}
		else {
			if (scholarships.length == 0) {
				res.write('<p class="ml-2">There are no scholarships</p>');
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

					res.write('<td>' + scholarship._id + '</td>');
					res.write('<td>' + scholarship.name + '</td>');
					res.write('<td>' + scholarship.org + '</td>');
					res.write('<td>' + scholarship.dollarAmount + '</td>');
					if (scholarship.dueDate) {
						res.write('<td>' + scholarship.dueDate.toISOString().slice(0, 10) + '</td>');
					} else {
						res.write('<td>' + 'Unknown' + '</td>');
					}
					res.write('<td>' +
						` <a class="btn btn-danger btn-sm" href="#" onclick="if (confirm('Delete scholarship &quot;` + scholarship.name + `&quot;?')) { window.location = '/delete?_id=` + scholarship.id + `' }">Delete</a>` +
						'</td>'
					);
					res.write("<td>" +
						' <a class="btn btn-warning btn-sm" href="/edit?_id=' + scholarship._id + '"\>Edit</a>' +
						'</td>'
					);
					res.write("<td>" +
					' <a class="btn btn-info btn-sm" href="/viewDetail?_id=' + scholarship._id + '">ViewDetail</a>' +
					'</td>'
					);

					res.write('</tr>');

				});
				res.write('</table>');
				res.end();
			}
		}
	}).sort({ 'dollarAmount': 'desc' }); // this sorts them BEFORE rendering the results

	res.write(' </div> </body></html>');

});

app.use('/viewDetail', (req, res) => {
	res.type('html').status(200);
	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.type('html').write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to find scholarship: ${id}`)
		Scholarship.findOne({'_id':id}, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.write("Unexpected error")
			}
			else if (!scholarship) {
				console.log(`Could not find scholarship with id ${id}`)
				
				res.write(getPageOutline());
				res.write("<div class=\"container mt-2 pl-2\">");
				res.write("<p>Could not find scholarship with id" + id +"</p>");
				res.write("<a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
				res.write(' </div> </body></html>');
				res.end();
				
			}
			else {
				console.log(`Found scholarship with id ${scholarship._id}`)

				res.write(getPageOutline());
				res.write(getCardHTML(scholarship));
				res.end();

			}
		});
	}
 });


app.use('/export', (req, res) => {
	let filename = 'export.csv';
	let absPath = path.join(__dirname, '/exports/', filename);
	let relPath = path.join('./exports', filename); // path relative to server root

	// Get all scholarships in the database
	Scholarship.find({}, (err, scholarships) => {
		if (err) {
			console.log('uh oh' + err)
			return res.write(err)
		}
		else {
			// Create a temporary file with the CSV content of the database
			fs.writeFile(relPath, toCSV(scholarships), (err) => {
				if (err) {
					console.log(err)
				}
				console.log(`Writing the file to ${absPath}`)
				// After downloading, redirect back to the homepage
				res.set({
					'Location': '/all'
				});
				// Download the file
				res.download(absPath, (err) => {
					if (err) {
						console.log(err)
					}
					console.log('Download finished!')
					// Delete the temporary file
					fs.unlink(relPath, (err) => {
						if (err) {
							console.log(err)
						}
						console.log(`Removed file ${filename}.`)
					})
				})
			})
		}
	})
})

// Takes a list of scholarship objects from the database and returns a string in CSV format.
function toCSV(scholarships) {
	console.log(`Making CSV out of ${scholarships.length} scholarships`)
	let columns = ['_id', 'name', 'org', 'description', 'dollarAmount', 'approvalStatus', 'dueDate', 'gpaRequirement']
	// Start CSV off with column headers
	let csv = columns.join(',') + '\n'
	scholarships.forEach(scholarship => {
		columns.forEach(key => {
			csv += scholarship[key]
			if (key != columns[columns.length - 1]) {
				csv += ','
			}
		})
		csv += '\n'
	})

	return csv
}

// Create the header
function getPageOutline() {
	let template = '';
	// Add brand
	template += '<nav class="navbar navbar-expand-lg navbar-dark bg-dark"> <a class="navbar-brand" href="/">Scholarshipr</a>'

	// Add navigation section
	template += '<div class="collapse navbar-collapse" id="navbarNavAltMarkup"> <div class="navbar-nav">'

	template += '<a class="nav-item nav-link" href="/all">All</a>'
	template += '<a class="nav-item nav-link" href="/suggested">Suggested</a>'
	template += '<a class="nav-item nav-link" href="/public/create.html">Add</a>'
	template += '<a class="nav-item nav-link" href="/export">Export</a>'

	template += '</div></div></nav>'

	// Add stylesheet
	template += '<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">'
	return template;
}
function getCardHTML(scholarship) {

	let name = scholarship.name;
	let description = scholarship.description;
	let org = scholarship.org;
	let dollarAmount = scholarship.dollarAmount;
	let approvalStatus = scholarship.approvalStatus;
	let dueDate = scholarship.dueDate;
	let gpaRequirement = scholarship.gpaRequirement;

	approvalEmoji = "❓"
	if (approvalStatus == "true") {
		approvalEmoji = "✅";
	} else if (approvalStatus == "false") {
		approvalStatus = "❌";
	}

	let returnableString = "<div class=\"col d-flex justify-content-center\">";
	returnableString += "<div class=\"card\" style=\"width: 50rem;\"> <div class=\"card-body\">    <h5 class=\"card-title\">";
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
		returnableString += "Due Date: " + scholarship.dueDate.toISOString().slice(0, 10);
	} else {
		returnableString += "Due Date: " + "Unknown";
	}

	returnableString += "</li>    <li class=\"list-group-item\">";
	returnableString += "GPA Requirement: " + gpaRequirement;
	returnableString += "</li> </ul>  <div class=\"mx-auto\"><div class=\"card-body\">";
	
	if(scholarship.approvalStatus=="false"){
		returnableString += `<td> <a class="btn btn-success btn-sm mr-1" href="#" onclick="if (confirm('Approve scholarship &quot;` + scholarship.name + `&quot;?')) { window.location = '/approve?_id=` + scholarship.id + `' }">Approve</a>` +'</td>';
	}
						
	returnableString += "<a class=\"btn btn-warning btn-sm mr-1\" href=\"/edit?_id=" + scholarship._id + "\">Edit</a>";
	returnableString += `<a class="btn btn-danger btn-sm" href="#" onclick="if (confirm('Delete scholarship &quot;` + scholarship.name + `&quot;?')) { window.location = '/delete?_id=` + scholarship.id + `' }">Delete</a>`;
	returnableString += "</div></div></div></div>";

	return returnableString;
}

app.use('/delete', (req, res) => {
	res.type('html').status(200);

	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to delete scholarship: ${id}`)
		Scholarship.findOneAndDelete({'_id':id}, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.write("Unexpected error")
			}
			else if (!scholarship) {
				// A strange error I can't debug happens here. Recreate by
				// visiting http://localhost:3000/delete?_id=5
				console.log(`Could not find scholarship with id ${id}`)
				res.write(getPageOutline());
				res.write("<div class=\"container mt-2 pl-2\">");
				res.write(`Could not find scholarship with id ${id}`);
				res.write("<br/><a class=\"btn btn-info btn-sm\" href=\"/all\">Return</a>");
				res.write(' </div> </body></html>');
				
				res.end();
			}
			else {
				console.log(`Deleted scholarship with id ${scholarship._id}`)
				res.redirect(302, '/all');
			}
		});
	}
});


app.use('/approve', (req, res) => {
	res.type('html').status(200);
	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.type('html').write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to approve scholarship: ${id}`)
		var action = {'$set': {approvalStatus: "true"}}
		Scholarship.findOneAndUpdate({'_id':id}, action, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.write("Unexpected error")
			}
			else if (!scholarship) {
				console.log(`Could not find scholarship with id ${id}`)
				return res.write(`Could not find scholarship with id ${id}`)
			}
			else {
				console.log(`Approved scholarship with id ${scholarship._id}`)
			}
		});
	}
    res.redirect(302, '/all');
});

app.use('/approve', (req, res) => {
	if (!req.query._id) {
		console.log('uh oh, no id in the query parameters')
		return res.type('html').write('uh oh, no id in the query parameters')
	} else {
		const id = req.query._id
		console.log(`Trying to approve scholarship: ${id}`)
		var action = {'$set': {approvalStatus: "true"}}
		Scholarship.findOneAndUpdate({'_id':id}, action, (err, scholarship) => {
			if (err) {
				console.log("Unexpected error")
				return res.type('html').write("Unexpected error")
			}
			else if (!scholarship) {
				console.log(`Could not find scholarship with id ${id}`)
				return res.type('html').write(`Could not find scholarship with id ${id}`)
			}
			else {
				console.log(`Approved scholarship with id ${scholarship._id}`)
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
		else {
			// construct an array out of the result
			var returnArray = [];
			scholarships.forEach((scholarship) => {
				returnArray.push({ "name": scholarship.name, 
				"org": scholarship.org,
				"description": scholarship.description,
				"dollarAmount": scholarship.dollarAmount,
				"approvalStatus": scholarship.approvalStatus,
				"dueDate": scholarship.dueDate,
				"gpaRequirement": scholarship.gpaRequirement});
			});
			// send it back as JSON Array
			res.json(returnArray);
		}

	});
});

/*************************************************/

app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/all'); });



app.listen(3000, () => {
	console.log('Listening on port 3000');
});
