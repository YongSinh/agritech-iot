// WebSocketContext.js
import { createContext, useState, useEffect, useContext } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const [data, setData] = useState(() => {
    const savedData = localStorage.getItem("MQTT_DATA");
    return savedData ? JSON.parse(savedData) : [];
  });

  useEffect(() => {
    const newClient = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8083/iot/ws"),
      onConnect: () => {
        newClient.subscribe("/topic/genMessage", (message) => {
          const newMessage = JSON.parse(message.body);
          setData(prevData => {
            const updatedData = [newMessage, ...(prevData || [])];
            localStorage.setItem("MQTT_DATA", JSON.stringify(updatedData));
            return updatedData;
          });
        });
      },
      onStompError: (frame) => {
        console.error("Broker error:", frame.headers["message"], frame.body);
      },
    });

    newClient.activate();

    return () => {
      newClient.deactivate();
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ data }}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => useContext(WebSocketContext);