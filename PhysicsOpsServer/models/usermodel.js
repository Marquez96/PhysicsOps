exports = module.exports = function(app, mongoose) {

	var user = new mongoose.Schema({
        events: [
            {
              cal: {
                year: Number,
                month: Number,
                dayOfMonth: Number,
                hourOfDay: Number,
                minute: Number,
                second: Number
              },
              day: Number,
              duration: Number,
              id: Number,
              importance: Number,
              month: Number,
              title: String,
              year: Number
            }
          ],
          friends: [
            {
              id: Number,
              idUserFrom: String,
              idUserTo: String,
              state: String
            },
            {
              id: Number,
              idUserFrom: String,
              idUserTo: String,
              state: String
            }
          ],
          lastUpdate: {
            year: Number,
            month: Number,
            dayOfMonth: Number,
            hourOfDay: Number,
            minute: Number,
            second: Number
          },
          idUser: Number,
          mail: String,
          name: String,
          password: String,
          surname: String,
          userName: String
	});

	mongoose.model('users', user);
};
