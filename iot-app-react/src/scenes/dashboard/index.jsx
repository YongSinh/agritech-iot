import {
  Box,
  Button,
  IconButton,
  Typography,
  useMediaQuery,
  useTheme,
  Select,
  MenuItem,
  InputLabel,
} from "@mui/material";
import {
  Header,
  StatBox,
  LineChart,
  ProgressCircle,
  BarChart,
  GeographyChart,
} from "../../components";
import {
  DownloadOutlined,
  Traffic,
  CalendarToday
} from "@mui/icons-material";
import DevicesIcon from '@mui/icons-material/Devices';
import { tokens } from "../../theme";
import { useRequest } from "../../config/api/request";
import { useState, useEffect } from "react";
import SockJS from "sockjs-client";
import Swal from "sweetalert2";
import DeviceStatusDialog from './DeviceStatusDialog';
import { Client } from "@stomp/stompjs";
import ModelForm from "./modelForm";
import dayjs from "dayjs";
import MyBarChart from "../../components/MyBarChart";

function Dashboard() {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const isXlDevices = useMediaQuery("(min-width: 1260px)");
  const isMdDevices = useMediaQuery("(min-width: 724px)");
  const isXsDevices = useMediaQuery("(max-width: 436px)");
  const [devices, setDevices] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [message, setMessage] = useState(null);
  const [deviceIds, setDeviceIds] = useState([]);
  const [statusType, setStatusType] = useState([]);
  const [mqttMesData, setMqttMesData] = useState([]);
  const [open, setOpen] = useState(false);
  const token = localStorage.getItem("access_token");
  const [limit, setLimit] = useState('10');


  const barData = [
    {
      "label": "Soil Temp",
      "value": 28.52,
      "id": "soil_temperature"
    },
    {
      "label": "Ambient Temp",
      "value": 28.50,
      "id": "ambien_temperature"
    },
    {
      "label": "Voltage",
      "value": 33.44,
      "id": "voltage"
    }
  ];

  const getListDevice = async () => {
    const result = await request("/iot/v1/device/total-status", "GET", null);
    if (result) {
      setDevices(result.data);
    }
  };


  // Open the form dialog
  const handleClickOpen = () => {
    setOpen(true);
  };

  // Close the form dialog
  const handleClose = () => {
    setOpen(false);
  };

  const getListStatusType = async () => {
    const result = await request("/iot/v1/devices/get-status-type", "GET", null);
    if (result) {
      setStatusType(result.data);
    }
  };


  const handleChange = (event) => {
    const newLimit = event.target.value;
    setLimit(newLimit);
    getMqttMessage(newLimit);
  };

  const getMqttMessage = async (currentLimit) => {
    const payload = {
      "topic": "mqtt_out",
      "limit": currentLimit
    };
    const result = await request("/api/log/v1/sensor-logs/filter", "POST", payload);
    if (result) {
      setMqttMesData(result.data);
    }
  };


  const getAllDeviceIds = async () => {
    const result = await request("/iot/v1/device/ids", "GET", null);
    if (result) {
      setDeviceIds(result?.data)
    }
  };

  // Example usage when you receive a message
  const handleNewMessage = (message) => {
    setMessage(message);
    setDialogOpen(true);
  };

  useEffect(() => {
    getListDevice();
    getAllDeviceIds();
    getListStatusType();
    getMqttMessage(limit);
  }, []);

  useEffect(() => {

    const newClient = new Client({
      webSocketFactory: () => new SockJS("/iot/ws"),
      // connectHeaders: {
      //   Authorization: `Bearer ${token}`, // 👈 Pass token here
      // },
      onConnect: () => {
        newClient.subscribe("/topic/public", (message) => {
          const newMessage = JSON.parse(message.body);
          handleNewMessage(newMessage);
          getMqttMessage(limit)
        });
      },
      onStompError: (frame) => {
        console.error("Broker error:", frame.headers["message"], frame.body);
      },
    });

    newClient.activate();
    setStompClient(newClient);

    return () => {
      newClient.deactivate();
    };
  }, []);


  const handleSubmit = async (formData) => {
    //console.log(formData)
    const result = await request("/iot/v1/control-logs/check-device", "POST", formData);
    handleClose();
    if (result.code = ! "SUC-000") {
      Swal.fire({
        title: "Error!",
        text: result.message,
        icon: "error",
        showConfirmButton: false,
        timer: 1500,
      });
    }
  };

  const menuItems = [
    { value: 10, label: "Ten" },
    { value: 20, label: "Twenty" },
    { value: 30, label: "Thirty" },
    { value: 50, label: "Fifty" }
  ];


  return (
    <Box m="20px">
      <DeviceStatusDialog
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        message={message}
      />
      <ModelForm
        open={open}
        deviceIds={deviceIds}
        statusType={statusType}
        handleClose={handleClose}
        handleSubmit={handleSubmit}
      />
      <Box display="flex" justifyContent="space-between">
        <Header title="DASHBOARD" subtitle="Welcome to your dashboard" />
        {!isXsDevices && (
          <Box>
            <Button
              variant="contained"
              sx={{
                bgcolor: colors.blueAccent[700],
                color: "#fcfcfc",
                fontSize: isMdDevices ? "14px" : "10px",
                fontWeight: "bold",
                p: "10px 20px",
                mt: "18px",
                transition: ".3s ease",
                ":hover": {
                  bgcolor: colors.blueAccent[800],
                },
              }}
              onClick={handleClickOpen}
              startIcon={<DownloadOutlined />}
            >
              GET REPORTS
            </Button>
          </Box>
        )}
      </Box>

      {/* GRID & CHARTS */}
      <Box
        display="grid"
        gridTemplateColumns={
          isXlDevices
            ? "repeat(12, 1fr)"
            : isMdDevices
              ? "repeat(6, 1fr)"
              : "repeat(3, 1fr)"
        }
        gridAutoRows="140px"
        gap="20px"
      >
        {/* Statistic Items */}
        {/* <Box
          gridColumn="span 3"
          bgcolor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={devices?.totalDevices ?? 'Default title'}
            subtitle="Total Online"
            progress="100"
            increase="100%"
            icon={
              <DevicesIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>
        <Box
          gridColumn="span 3"
          bgcolor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title={devices?.totalDevicesOnline ?? 'Default title'}
            subtitle="Device Online"
            progress="0.75"
            increase="+14%"
            icon={
              <DevicesIcon
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>
        <Box
          gridColumn="span 3"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title="431,225"
            subtitle="Schedule"
            progress="0.50"
            increase="+21%"
            icon={
              <CalendarToday
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box>
        <Box
          gridColumn="span 3"
          backgroundColor={colors.primary[400]}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          <StatBox
            title="1,325,134"
            subtitle="Traffic Received"
            progress="0.80"
            increase="+43%"
            icon={
              <Traffic
                sx={{ color: colors.greenAccent[600], fontSize: "26px" }}
              />
            }
          />
        </Box> */}

        {/* ---------------- Row 2 ---------------- */}

        {/* Line Chart */}
        <Box
          gridColumn={
            isXlDevices ? "span 8" : isMdDevices ? "span 6" : "span 3"
          }
          gridRow="span 2"
          bgcolor={colors.primary[400]}
        >
          <Box
            mt="25px"
            px="30px"
            display="flex"
            justifyContent="space-between"
          >
            <Box>
              <Typography
                variant="h5"
                fontWeight="600"
                color={colors.gray[100]}
              >
                Revenue Generated
              </Typography>
              <Typography
                variant="h5"
                fontWeight="bold"
                color={colors.greenAccent[500]}
              >
                $59,342.32
              </Typography>
            </Box>
            <IconButton>
              <DownloadOutlined
                sx={{ fontSize: "26px", color: colors.greenAccent[500] }}
              />
            </IconButton>
          </Box>
          <Box height="250px" mt="-20px">
            <LineChart isDashboard={true} />
          </Box>
        </Box>

        {/* Transaction Data */}
        <Box
          gridColumn={isXlDevices ? "span 4" : "span 3"}
          gridRow="span 2"
          bgcolor={colors.primary[400]}
          overflow="auto"
        >
          <Box
            display="flex"
            alignItems="center"
            justifyContent="space-between"
            borderBottom={`4px solid ${colors.primary[500]}`}
            p="15px">
            <Typography color={colors.gray[100]} variant="h5" fontWeight="600">
              Recent MQTT Message
            </Typography>
            <Select
              labelId="limit-select-standard"
              id="limit-select-standard"
              value={limit}
              onChange={handleChange}
              defaultValue="10"
              label="Limit Mesaage"
            >
              {menuItems.map((item) => (
                <MenuItem key={item.value} value={item.value}>
                  {item.label}
                </MenuItem>
              ))}
            </Select>
          </Box>

          {mqttMesData.map((data, index) => (
            <Box
              key={`${index}`}
              display="flex"
              alignItems="center"
              justifyContent="space-between"
              borderBottom={`4px solid ${colors.primary[500]}`}
              p="15px"
            >
              <Box>
                <Typography
                  color={colors.greenAccent[500]}
                  variant="h5"
                  fontWeight="600"
                >
                  {data.data?.device || 'N/A'}
                </Typography>
                <Typography color={colors.gray[100]}>
                  {data.data?.status || 'N/A'}
                </Typography>
              </Box>
              <Typography color={colors.gray[100]}>
                {data.data?.state || 'N/A'}
              </Typography>
              <Typography color={colors.gray[100]}>
                {dayjs(data?.date_time).format('YYYY-MM-DD h:mm A') || 'N/A'}
              </Typography>
              <Box
                bgcolor={colors.greenAccent[500]}
                p="5px 10px"
                borderRadius="4px"
              >
                {data.data?.value || 'N/A'}
              </Box>
            </Box>
          ))}
        </Box>

        {/* Revenue Details */}
        <Box
          gridColumn={isXlDevices ? "span 4" : "span 3"}
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
          p="30px"
        >
          <Typography variant="h5" fontWeight="600">
            Campaign
          </Typography>
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            mt="25px"
          >
            <ProgressCircle size="125" />
            <Typography
              textAlign="center"
              variant="h5"
              color={colors.greenAccent[500]}
              sx={{ mt: "15px" }}
            >
              $48,352 revenue generated
            </Typography>
            <Typography textAlign="center">
              Includes extra misc expenditures and costs
            </Typography>
          </Box>
        </Box>

        {/* Bar Chart */}
        <Box
          gridColumn={isXlDevices ? "span 4" : "span 3"}
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
        >
          <Typography
            variant="h5"
            fontWeight="600"
            sx={{ p: "30px 30px 0 30px" }}
          >
           Sensor Quantity
          </Typography>
          <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="250px"
            mt="-20px"
          >
            <MyBarChart data={message?.data || []} />
            {/* <BarChart isDashboard={true} /> */}
          </Box>
        </Box>
        <Box
          gridColumn={isXlDevices ? "span 4" : "span 3"}
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
        >
          <Typography
            variant="h5"
            fontWeight="600"
            sx={{ p: "30px 30px 0 30px" }}
          >
            Sensor Quantity
          </Typography>
          <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="250px"
            mt="-20px"
          >
            <BarChart isDashboard={true} />
          </Box>
        </Box>

        {/* Geography Chart */}
        {/* <Box
          gridColumn={isXlDevices ? "span 4" : "span 3"}
          gridRow="span 2"
          backgroundColor={colors.primary[400]}
          padding="30px"
        >
          <Typography variant="h5" fontWeight="600" mb="15px">
            Geography Based Traffic
          </Typography>
          <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="200px"
          >
            <GeographyChart isDashboard={true} />
          </Box>
        </Box> */}
      </Box>
    </Box>
  );
}

export default Dashboard;
