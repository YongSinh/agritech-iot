import { Box, useTheme, Button } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";
import dayjs from "dayjs";
import Swal from "sweetalert2";

const IntervalSchedule = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [intervalSchedule, setIntervalSchedule] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deviceIds, setDeviceIds] = useState([]);
  const [open, setOpen] = useState(false);
  const [initialData, setInitialData] = useState(null);
  const [edit, setEdit] = useState(false);

  // Open the form dialog
  const handleClickOpen = () => {
    setOpen(true);
    setEdit(false)
  };

  // Close the form dialog
  const handleClose = () => {
    setOpen(false);
    setEdit(false)
  };

  const handleUpdate = (value) => {
    setInitialData(value);
    setOpen(true);
    setEdit(true)
  };

  const handleSubmit = async (formData) => {
    // console.log(formData)
    // You can now send the formData to your API or perform other actions
    let url = edit ? "/iot/v1/update-interval-schedule" : "/iot/v1/create-interval-schedule";
    let method = "post";

    const result = await request(url, method, formData);
    if (result.code === "SUC-000") {
      Swal.fire({
        title: "Success!",
        text: "Your has been saved",
        icon: "success",
        showConfirmButton: false,
        timer: 1500,
      });
      setLoading(false);
      getListIntervalSchedule()
      handleClose(); // Close the dialog after submission
    } else {
      Swal.fire({
        title: "Error!",
        text: result.message,
        icon: "error",
        showConfirmButton: true,
        timer: 3000,
      });
      handleClose(); // Close the dialog after submission
      setLoading(false);
    }
  };

  useEffect(() => {
    getListIntervalSchedule()
    getAllDeviceIds()
  }, []);


  const getAllDeviceIds = async () => {
    const result = await request("/iot/v1/device-ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
    }
  };


  const getListIntervalSchedule = async () => {
    const result = await request("/iot/v1/interval-schedule", "GET", null);
    if (result) {
      setIntervalSchedule(result.data)
      setLoading(false)
    }
  };
  const columns = [
    { field: "id", headerName: "ID" },
    {
      field: "interval",
      headerName: "Interval",
      flex: 1,
      cellClassName: "name-column--cell",
    },
    {
      field: "device_id",
      headerName: "Device ID",
      flex: 1,
    },
    {
      field: "duration",
      headerName: "Duration",
      flex: 1,
      cellClassName: "name-column--cell",
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
    },
    {
      field: "runDatetime",
      headerName: "Run Date time",
      flex: 1,
      renderCell: ({ row: { runDatetime } }) => {
        return (
          <span>{dayjs(runDatetime).format('YYYY-MM-DD h:mm A')}</span>
        );
      },
    },
    {
      headerName: "Action",
      flex: 1,
      renderCell: ({ row }) => {
        return (
          <Button
            color="secondary"
            variant="contained"
            onClick={() => handleUpdate(row)}
          >
            Edit
          </Button>
        );
      },
    }
  ];
  return (
    <Box m="20px">
      <Header title="INTERVAL SCHEDULE" subtitle="List of Interval Schedule" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add INTERVAL SCHEDULE
      </Button>
      <ModelForm
        open={open}
        handleClose={handleClose}
        handleSubmit={handleSubmit} // Pass the submit handler
        colors={colors}
        deviceIds={deviceIds}
        initialData={initialData}
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
          rows={intervalSchedule}
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

export default IntervalSchedule;
