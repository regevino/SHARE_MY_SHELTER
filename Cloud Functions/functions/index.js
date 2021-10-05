const functions = require("firebase-functions");

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });


// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');

const pikudHaoref = require('pikud-haoref-api');
admin.initializeApp();

function handle_alerts(city_metadata) {
    functions.logger.log(city_metadata);
    if (city_metadata){
        send_message(city_metadata, false);
    }
}

function send_message(cities_metadata, test)
{
    // The topic name can be optionally prefixed with "/topics/".
    let topic = "";
    if (test){
        topic = 'Test-Alerts';
    }
    else
    {
        topic = 'Alerts';
    }
    functions.logger.log("Sending to topic: ", topic);

    const message = {
        data: {
            'cities_metadata': cities_metadata
        },
        topic: topic
    };

    // Send a message to devices subscribed to the provided topic.
    admin.messaging().send(message)
        .then((response) => {
            // Response is a message ID string.
            functions.logger.log('Successfully sent message:', response);
        })
        .catch((error) => {
            functions.logger.log('Error sending message:', error);
        });

}


function check_for_alerts(res)
{
    pikudHaoref.getActiveRocketAlertCities((err, alert_cities) => {
        if (err) {
            return  Promise.resolve(functions.logger.log('Retrieving active rocket alert cities failed: ', err));
        }

        // Alert cities header
        functions.logger.log('Currently active rocket alert cities:');

        // Log the alert cities (if any)
        functions.logger.log(alert_cities);

        // Line break for readability
        functions.logger.log();
    }, {});
    pikudHaoref.getCityMetadata((err, metadata) => {
        if (err) {
            functions.logger.log('Retrieving active rocket alert metadata failed: ', err);
            if(res)
            {
                res.json({"result": err})
            }
            return;
        }
        functions.logger.log('Currently active rocket alert cities:');
        functions.logger.log(metadata);
        // Line break for readability
        functions.logger.log();
        handle_alerts(metadata);
        if (res)
        {
            res.json({result: metadata});
        }
    }, {googleMapsApiKey: functions.config().googlemaps.key});


    return Promise.resolve({});
}
functions.pubsub.schedule('every 1 minutes').onRun((context => {
    check_for_alerts();
}));

exports.checkAlerts = functions.https.onRequest(async (req, res) => {
    check_for_alerts(res);
});

exports.sendTestAlert =  functions.https.onRequest(async (req, res) => {
    res.json({result: "success"});
    return Promise.resolve(send_message('test_message', true));
});

exports.sendMessage = functions.https.onRequest(async (req, res) => {
    res.json({result: "success"});
    return Promise.resolve(send_message('test_message', false));
});