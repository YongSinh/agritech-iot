import axios from "axios";
import { toast, Bounce } from 'react-toastify';

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

const notifyError = (message) => {
  toast.error(message, {
    position: "top-center",
    autoClose: 5000,
    hideProgressBar: false,
    closeOnClick: false,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light",
    transition: Bounce,
  });
};

const notifyWarning = (message) => {
  toast.warn(message, {
    position: "top-center",
    autoClose: 5000,
    hideProgressBar: false,
    closeOnClick: false,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light",
    transition: Bounce,
  });
};
// Wrap the request function in a hook to use notifications
export const useRequest = () => {

  const request = async (url, method, param) => {
    const headers = {
      "Content-Type": param instanceof FormData ? "multipart/form-data" : "application/json",
      accept: param instanceof FormData ? "application/json" : "*/*",
      "correlation_id": self.crypto.randomUUID()
      // Authorization: `Bearer ${getLocalAccessToken()}`,
    };

    try {
      const response = await axios({
        url: `${config.base_server}${url}`,
        method,
        data: param,
        headers,
        timeout: 10000, // Timeout after 10 seconds (10000ms)
      });

      if (response.data.code === 400 || response.data.code === 503) {
        notifyWarning(response.data.message);
      } else {
        return response.data;
      }
    } catch (err) {
      if (err.code === 'ECONNABORTED') {
        notifyError("Request timed out. Please try again.");
      } else {
        const status = err.response?.status;
        switch (status) {
          case STATUS_CODES.NOT_FOUND:
            notifyError(MESSAGES.NOT_FOUND);
            break;
          case STATUS_CODES.INTERNAL_SERVER_ERROR:
            notifyError(err.message);
            break;
          case STATUS_CODES.UNAUTHORIZED:
            notifyError(MESSAGES.UNAUTHORIZED);
            break;
          case STATUS_CODES.FORBIDDEN:
            notifyWarning(MESSAGES.FORBIDDEN);
            break;
          default:
            notifyError(err.message);
            break;
        }
      }
      return false;
    } finally {
      console.log("Request completed");
    }
  };

  return { request };
};