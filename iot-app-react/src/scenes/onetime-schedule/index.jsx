import { Box, Button, useTheme, Stack } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";
import dayjs from "dayjs";
import Swal from "sweetalert2";
import SyncIcon from '@mui/icons-material/Sync';
import SyncDisabledIcon from '@mui/icons-material/SyncDisabled';
import { toast } from 'react-toastify';
import { useSelectedData } from '../../utils/hooks'
import MaterialUISwitch from "../../components/Switch"

const OnetimeSchedule = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [onetimeSchedule, setOnetimeSchedule] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deviceIds, setDeviceIds] = useState([]);
  const [open, setOpen] = useState(false);
  const [initialData, setInitialData] = useState(null);
  const [edit, setEdit] = useState(false);
  const [status, setStatus] = useState(false);
  const [selectedRows, setSelectedRows] = useState([]);
  const selectedRowData = useSelectedData(onetimeSchedule, selectedRows);

  const handleChangeStatus = async (id, newStatus) => {
    setOnetimeSchedule(onetimeSchedule.map(row =>
      row.id === id ? { ...row, status: newStatus } : row
    ));

    const message = newStatus ? "The schedule is now ON" : "The schedule is now OFF";

    const body = {
      "id": id,
      "status": newStatus,
      "batchSize": 500
    }

    // You can now send the formData to your API or perform other actions
    let url = "/iot/api/v1/onetime-Schedule/update-single-status";
    let method = "post";

    const result = await request(url, method, body);
    if (result) {
      toast.success(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light"
      });
      getListRepeatSchedules()
    } else {
      toast.error('Failed to update the schedule status. Please try again ', {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light"
      });
    }
  };


  const handleChangeAllStatus = async (newStatus) => {
    setStatus(newStatus)
    const message = newStatus ? "The all schedule is now ON" : "The all schedule is now OFF";
    const selectedIds = selectedRowData.map(item => item.id);
    if (selectedIds.length === 0) {
      toast.warn("Please select at least one item to update", {
        position: "top-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light"
      });
      return; // Exit early if no IDs are selected
    }
    const body = {
      "id": 10,
      "ids":selectedIds,
      "status": newStatus,
      "batchSize": 500
    }

    // You can now send the formData to your API or perform other actions
    let url = "/iot/api/v1/onetime-Schedule/update-multiple-status";
    let method = "post";

    const result = await request(url, method, body);
    if (result) {
      toast.success(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light"
      });
      getListOnetimeSchedule()
    } else {
      toast.error('Failed to update the schedule status. Please try again ', {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light"
      });
    }
  };

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
    // You can now send the formData to your API or perform other actions
    let url = edit ? "/iot/api/v1/update-onetime-Schedule" : "/iot/api/v1/create-onetime-Schedule";
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
      getListOnetimeSchedule()
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
    getListOnetimeSchedule()
    getAllDeviceIds()
  }, []);

  const getAllDeviceIds = async () => {
    const result = await request("/iot/api/v1/device-ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
    }
  };

  const getListOnetimeSchedule = async () => {
    const result = await request("/iot/api/v1/one-time-schedules", "GET", null);
    if (result) {
      setOnetimeSchedule(result.data)
      setLoading(false)
    }
  };

  const handleSelectionChange = (newSelection) => {
    setSelectedRows(newSelection);
  };

  const columns = [
    { field: "id", headerName: "ID" },
    {
      field: "deviceId",
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
    },
    {
      headerName: "Status",
      field: "status",
      flex: 1,
      renderCell: ({ row }) => {
        return (
          <MaterialUISwitch
            status={row.status}
            onChange={(newStatus) => handleChangeStatus(row.id, newStatus)}
          />
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
      <Header title="ONETIME SCHEDULE" subtitle="List of Onetime Schedule" />
      <Stack direction="row" spacing={2}>
        <Button color="secondary" variant="contained" onClick={handleClickOpen}>
          Add ONETIME SCHEDULE
        </Button>
        <Button
          variant="contained"
          color={status ? "error" : "success"}
          endIcon={status ? <SyncDisabledIcon /> : <SyncIcon />}
          onClick={() => handleChangeAllStatus(!status)}
        >
          {status ? "Turn Off Schedule" : "Turn On Schedule"}
        </Button>
      </Stack>
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
          onRowSelectionModelChange={handleSelectionChange}
          rowSelectionModel={selectedRows}
        />
      </Box>
    </Box>
  );
};

export default OnetimeSchedule;
