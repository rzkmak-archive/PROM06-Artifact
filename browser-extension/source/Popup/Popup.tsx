import * as React from 'react';
// import axios from 'axios';
import {useEffect, useState} from "react";

import './styles.scss';
import {browser} from "webextension-polyfill-ts";

const Popup: React.FC = () => {
    const [email, setEmail] = useState('');
    const [keyExists, setKeyExists] = useState(false);

    useEffect(() => {
        // Get the key from local storage
        getIdentifierKey().then(
            r => {
                if (r) {
                    setKeyExists(true);
                }
            }
        );

        setInterval(() => {
            // console.log("polling started")
        }, 1000)
    }, []);

    const storeIdentifierKey = (identifierKey: string) => {
        browser
            .storage
            .local
            .set({"CYBER_BULLY_EXTENSION_USER_ID_KEY": identifierKey})
            .then(() => (console.log("session initiated")))
    }

    const getIdentifierKey = async (): Promise<string | undefined> => {
        try {
            const data = await browser.storage.local.get(
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

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        storeIdentifierKey(email);
        setKeyExists(true);

        // try {
        //     axios.get('https://your-api-endpoint').then(r => r.data);
        // } catch (error) {
        //     console.error('Error fetching data:', error);
        // }
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
                            <span>10</span>
                        </div>
                        <div className="information-item">
                            <label>Cyberbully Tweet Count:</label>
                            <span>10</span>
                        </div>
                        <div className="information-item">
                            <label>Last Alert:</label>
                            <span>20 Minutes Ago</span>
                        </div>
                    </div>
                )}
            </div>
        </section>
    );
};

export default Popup;
