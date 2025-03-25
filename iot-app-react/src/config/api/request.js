import axios from "axios";
import { useNotifications } from '@toolpad/core/useNotifications';

// Constants for status codes and messages
const STATUS_CODES = {
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
};

const MESSAGES = {
  NOT_FOUND: "Route Not Found!",
  UNAUTHORIZED: "401 Unauthorized!",
  FORBIDDEN: "You don't have permission to access this resource!",
};

export function getLocalAccessToken() {
  return localStorage.getItem("access_token");
}

export const config = {
  base_server: "http://localhost:8085",
  image_path: "",
  version: 1,
};

// Wrap the request function in a hook to use notifications
export const useRequest = () => {
  const { notify } = useNotifications();

  const request = async (url, method, param) => {
    const headers = {
      "Content-Type": param instanceof FormData ? "multipart/form-data" : "application/json",
       accept: param instanceof FormData ? "application/json" : "*/*",
       "correlation_id": self.crypto.randomUUID()
    //   Authorization: `Bearer ${getLocalAccessToken()}`,
    };

    try {
      const response = await axios({
        url: `${config.base_server}${url}`,
        method,
        data: param,
        headers,
      });

      if (response.data.code === 400 || response.data.code === 503) {
        notify({ severity: "warning", message: response.data.message });
      } else {
        return response.data;
      }
    } catch (err) {
      const status = err.response?.status;
      switch (status) {
        case STATUS_CODES.NOT_FOUND:
          notify({ severity: "error", message: MESSAGES.NOT_FOUND });
          break;
        case STATUS_CODES.INTERNAL_SERVER_ERROR:
          notify({ severity: "error", message: err.message });
          break;
        case STATUS_CODES.UNAUTHORIZED:
          notify({ severity: "warning", message: MESSAGES.UNAUTHORIZED });
          break;
        case STATUS_CODES.FORBIDDEN:
          notify({ severity: "warning", message: MESSAGES.FORBIDDEN });
          break;
        default:
          notify({ severity: "error", message: err.message });
          break;
      }
      return false;
    } finally {
      console.log("Request completed");
    }
  };

  return { request };
};