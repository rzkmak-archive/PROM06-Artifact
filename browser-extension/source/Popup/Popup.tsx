import * as React from 'react';
import axios from 'axios';
import {useEffect, useState} from "react";

import './styles.scss';
import {browser} from "webextension-polyfill-ts";

const Popup: React.FC = () => {
    const [email, setEmail] = useState('');
    const [keyExists, setKeyExists] = useState(false);
    const [analyzedTweetCount, setAnalyzedTweetCount] = useState(0)
    const [cyberBullyTweetCount, setCyberBullyTweetCount] = useState(0)
    const [lastAlertTriggered, setLastAlertTriggered] = useState('')

    const startPolling = (sessionIdKey: string) => {
        setInterval(async () => {

            let result = await axios.get(
                'http://localhost:8080/v1/self/cyberbully/statistics',
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Session-Id': sessionIdKey
                    }
                }
            )

            if (result.status != 200) {
                console.error("error happen", result.data)
            } else {
                console.log(result.data, result.request.data)
                setAnalyzedTweetCount(result.data["analyzedCyberBullyCount"])
                setCyberBullyTweetCount(result.data["cyberBullyDetectedCount"])
                setLastAlertTriggered(result.data["lastAlert"])
            }
        }, 1000)
    }

    useEffect(() => {
        getIdentifierKey().then(
            r => {
                if (r != null) {
                    setKeyExists(true);
                    startPolling(r);
                }
            }
        );
    }, []);

    const storeIdentifierKey = (identifierKey: string) => {
        browser
            .storage
            .sync
            .set({"CYBER_BULLY_EXTENSION_USER_ID_KEY": identifierKey})
            .then(() => (console.log("session initiated")))
    }

    const clearIdentifierKey = () => {
        browser
            .storage
            .sync
            .remove("CYBER_BULLY_EXTENSION_USER_ID_KEY")

        setKeyExists(false)
    }

    const getIdentifierKey = async (): Promise<string | undefined> => {
        try {
            const data = await browser.storage.sync.get(
                "CYBER_BULLY_EXTENSION_USER_ID_KEY"
            );
            return data["CYBER_BULLY_EXTENSION_USER_ID_KEY"];
        } catch (error) {
            console.error("Error getting identifier key:", error);
            return undefined;
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(e.target.value);
    };

    const createSession = async (notificationTarget: string): Promise<string> => {
        try {
            let result = await axios.post(
                'http://localhost:8080/v1/self/cyberbully/initialize',
                {
                    "notificationTargetValue": notificationTarget,
                    "notificationTargetType": "EMAIL",
                    "thresholdDurationInSeconds": 500,
                    "thresholdCount": 5
                },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
            return Promise.resolve(result.data["sessionId"]);
        } catch (error) {
            console.error('Error:', error);
            return Promise.reject(error)
        }
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        createSession(email).then(sessionId => {
            storeIdentifierKey(sessionId);
            setKeyExists(true);
            startPolling(sessionId);
        });
    };

    return (
        <section id="popup">
            <h2>Cyberbully Twitter Detector</h2>

            <div>
                { !keyExists ? (
                    <form onSubmit={handleSubmit}>
                        <label htmlFor="email">Email for Target Notification</label>
                        <input
                            type="text"
                            id="email"
                            name="email"
                            value={email}
                            onChange={handleChange}
                            placeholder="Enter your email..."
                        />
                        <br/>
                        <button type="submit">Initialize</button>
                    </form>
                ) : (
                    <div className="information-display">
                        <div className="information-item">
                            <label>Analyzed Tweet Count: </label>
                            <span>${analyzedTweetCount}</span>
                        </div>
                        <div className="information-item">
                            <label>Cyberbully Tweet Count:</label>
                            <span>${cyberBullyTweetCount}</span>
                        </div>
                        <div className="information-item">
                            <label>Last Alert:</label>
                            <span>${lastAlertTriggered}</span>
                        </div>

                        <button onClick={clearIdentifierKey}>Clear Session</button>
                    </div>
                )}
            </div>
        </section>
    );
};

export default Popup;
