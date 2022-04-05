var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var scholarshipSchema = new Schema({
	name: {type: String, required: true, unique: true},
    org: {type: String, required: false, unique: false},
    description: {type: String, required: false, unique: false},
    dollarAmount: {type: Number, required: false, unique: false},
    // gender: {type: Enumerator, required: false, unique: false},
    approvalStatus: {type: String, required: true, unique: false},
    // race: {type: Enumerator, required: false, unique: false},
    // nationality: {type: Enumerator, required: false, unique: false},
    // athleticOrAcademic: {type: Enumerator, required: false, unique: false},
    dueDate: {type: Date, required: false, unique: false},
    gpaRequirement: {type: Number, required: false, unique: false},
    });

// export scholarshipSchema as a class called Scholarship
module.exports = mongoose.model('Scholarship', scholarshipSchema);

scholarshipSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}
