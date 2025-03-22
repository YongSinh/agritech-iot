import { Box, Button, useTheme } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";
import dayjs from "dayjs";

const OnetimeSchedule = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [onetimeSchedule, setOnetimeSchedule] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deviceIds, setDeviceIds] = useState([]);
  const [open, setOpen] = useState(false);

  // Open the form dialog
  const handleClickOpen = () => {
    setOpen(true);
  };

  // Close the form dialog
  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = (formData) => {
    console.log("Form Data Submitted:", formData);
    // You can now send the formData to your API or perform other actions
    handleClose(); // Close the dialog after submission
  };

  useEffect(() => {
    getListOnetimeSchedule()
    getAllDeviceIds()
  }, []);

  const getAllDeviceIds = async () => {
    const result = await request("/iot/api/v1/device-ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
      console.log("Data fetched:", result);
    }
  };

  const getListOnetimeSchedule = async () => {
    const result = await request("/iot/api/v1/one-time-schedules", "GET", null);
    if (result) {
      setOnetimeSchedule(result.data)
      setLoading(false)
      console.log("Data fetched:", result);
    }
  };
  const columns = [
    { field: "id", headerName: "ID" },
    {
      field: "device_id",
      headerName: "Device ID",
      flex: 1,
    },
    {
      field: "duration",
      headerName: "Duration",
      flex: 1,
    },
    {
      field: "time",
      headerName: "Time",
      flex: 1,
      renderCell: ({ row: { time, date } }) => {
        // Combine date and time into a single string
        const dateTimeString = `${date} ${time}`;

        // Parse and format using Day.js
        const formattedDateTime = dayjs(dateTimeString).format('h:mm A');

        return <span>{formattedDateTime}</span>;
      },
    },
    {
      field: "date",
      headerName: "Date",
      flex: 1,
    },
    {
      field: "readSensor",
      headerName: "Read Sensor",
      flex: 1,
    },
    {
      field: "turnOnWater",
      headerName: "Turn On Water",
      flex: 1,
    }
  ];
  return (
    <Box m="20px">
      <Header title="ONETIME SCHEDULE" subtitle="List of Onetime Schedule" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add ONETIME SCHEDULE
      </Button>
      <ModelForm
        open={open}
        handleClose={handleClose}
        handleSubmit={handleSubmit} // Pass the submit handler
        colors={colors}
        deviceIds={deviceIds}
      />
      <Box
        mt="40px"
        height="75vh"
        maxWidth="100%"
        sx={{
          "& .MuiDataGrid-root": {
            border: "none",
          },
          "& .MuiDataGrid-cell": {
            border: "none",
          },
          "& .name-column--cell": {
            color: colors.greenAccent[300],
          },
          "& .MuiDataGrid-columnHeaders": {
            backgroundColor: colors.blueAccent[700],
            borderBottom: "none",
          },
          "& .MuiDataGrid-virtualScroller": {
            backgroundColor: colors.primary[400],
          },
          "& .MuiDataGrid-footerContainer": {
            borderTop: "none",
            backgroundColor: colors.blueAccent[700],
          },
          "& .MuiCheckbox-root": {
            color: `${colors.greenAccent[200]} !important`,
          },
          "& .MuiDataGrid-iconSeparator": {
            color: colors.primary[100],
          },
        }}
      >
        <DataGrid
          rows={onetimeSchedule}
          columns={columns}
          loading={loading}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 10,
              },
            },
          }}
          checkboxSelection
        />
      </Box>
    </Box>
  );
};

export default OnetimeSchedule;
