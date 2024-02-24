import http from 'k6/http';
import { check } from 'k6';
import { randomString, randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export const options = {
    // A number specifying the number of VUs to run concurrently.
    vus: 1000,

    stages: [
        {
            duration: '5s', target: 10
        },
        {
            duration: '20s', target: 30
        },
        {
            duration: '20s', target: 50
        },
        {
            duration: '20s', target: 100
        },
        {
            duration: '20s', target: 1000
        }
    ]

    // The following section contains configuration options for execution of this
    // test script in Grafana Cloud.
    //
    // See https://grafana.com/docs/grafana-cloud/k6/get-started/run-cloud-tests-from-the-cli/
    // to learn about authoring and running k6 test scripts in Grafana k6 Cloud.
    //
    // ext: {
    //   loadimpact: {
    //     // The ID of the project to which the test is assigned in the k6 Cloud UI.
    //     // By default tests are executed in default project.
    //     projectID: "",
    //     // The name of the test in the k6 Cloud UI.
    //     // Test runs with the same name will be grouped.
    //     name: "script.js"
    //   }
    // },

    // Uncomment this section to enable the use of Browser API in your tests.
    //
    // See https://grafana.com/docs/k6/latest/using-k6-browser/running-browser-tests/ to learn more
    // about using Browser API in your test scripts.
    //
    // scenarios: {
    //   // The scenario name appears in the result summary, tags, and so on.
    //   // You can give the scenario any name, as long as each name in the script is unique.
    //   ui: {
    //     // Executor is a mandatory parameter for browser-based tests.
    //     // Shared iterations in this case tells k6 to reuse VUs to execute iterations.
    //     //
    //     // See https://grafana.com/docs/k6/latest/using-k6/scenarios/executors/ for other executor types.
    //     executor: 'shared-iterations',
    //     options: {
    //       browser: {
    //         // This is a mandatory parameter that instructs k6 to launch and
    //         // connect to a chromium-based browser, and use it to run UI-based
    //         // tests.
    //         type: 'chromium',
    //       },
    //     },
    //   },
    // }
};

// The function that defines VU logic.
//
// See https://grafana.com/docs/k6/latest/examples/get-started-with-k6/ to learn more
// about authoring k6 scripts.
//

export default function () {
    const headers = {
        headers: {
            'Content-Type': 'application/json',
        }
    };
    const randomWord = `${randomString(randomIntBetween(10,100))} ${randomString(randomIntBetween(10,100))} ${randomString(randomIntBetween(10,100))}`

    const screeningResponse = http.post('http://188.166.185.146:8000/v1/screening/twitter', JSON.stringify({
        value: randomWord
    }), headers);

    check(screeningResponse, {
        'status is 200': (r) => r.status === 200
    })
}

export function handleSummary(data) {
    return {
        "summary.html": htmlReport(data),
    };
}
