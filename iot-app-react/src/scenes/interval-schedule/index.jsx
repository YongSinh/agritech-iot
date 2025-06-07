import { Box, useTheme, Button, Stack, IconButton } from "@mui/material";
import { Header } from "../../components";
import DeleteIcon from '@mui/icons-material/Delete';
import { EditRounded } from "@mui/icons-material";
import { DataGrid } from "@mui/x-data-grid";
import SyncIcon from '@mui/icons-material/Sync';
import SyncDisabledIcon from '@mui/icons-material/SyncDisabled';
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";
import dayjs from "dayjs";
import Swal from "sweetalert2";
import { toast } from 'react-toastify';
import { useSelectedData } from '../../utils/hooks'
import MaterialUISwitch from "../../components/Switch"


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
  const [status, setStatus] = useState(false);
  const [selectedRows, setSelectedRows] = useState([]);
  const selectedRowData = useSelectedData(intervalSchedule, selectedRows);

  const handleChangeStatus = async (id, newStatus) => {
    setIntervalSchedule(intervalSchedule.map(row =>
      row.id === id ? { ...row, status: newStatus } : row
    ));

    const message = newStatus ? "The schedule is now ON" : "The schedule is now OFF";

    const body = {
      "id": id,
      "status": newStatus,
      "batchSize": 500
    }
  
    console.log(body)

    // You can now send the formData to your API or perform other actions
    let url = "/iot/v1/interval-schedule/update-single-status";
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
      getListIntervalSchedule()
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
      "ids": selectedIds,
      "status": newStatus,
      "batchSize": 500
    }

    // You can now send the formData to your API or perform other actions
    let url = "/iot/v1/interval-schedule/update-multiple-status";
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
      getListIntervalSchedule()
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


  const handleOnDelete = async (value) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!"
    });

    if (!result.isConfirmed) return;

    try {
      await request(`/iot/v1/interval-schedule/${value.id}`, "DELETE", null);
      await getListIntervalSchedule(); // Assuming this is async
      Swal.fire({
        title: "Deleted!",
        text: "Your record has been deleted.",
        icon: "success"
      });
    } catch (error) {
      Swal.fire({
        title: "Error!",
        text: "Failed to delete the item.",
        icon: "error"
      });
    }
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
    let url = edit ? "/iot/v1/interval-schedule/update" : "/iot/v1/interval-schedule/create";
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
    const result = await request("/iot/v1/device/ids", "GET", null);
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

  const handleSelectionChange = (newSelection) => {
    setSelectedRows(newSelection);
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
      field: "deviceId",
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
      headerName: "Actions",
      field: "actions",
      flex: 0.5,
      headerAlign: "center",
      align: "center",
      sortable: false,
      filterable: false,
      disableColumnMenu: true,
      renderCell: ({ row }) => {
        return (
          <Stack direction="row" spacing={0.5}>
            <IconButton aria-label="edit"
              color="secondary"
              onClick={() => handleUpdate(row)}
              size="large"
            >
              <EditRounded />
            </IconButton>
            <IconButton
              aria-label="delete"
              size="large"
              color="error"
              onClick={() => handleOnDelete(row)}
            >
              <DeleteIcon />
            </IconButton>
          </Stack>
        );
      },
    }
  ];
  return (
    <Box m="20px">
      <Header title="INTERVAL SCHEDULE" subtitle="List of Interval Schedule" />
      <Stack direction="row" spacing={2}>
        <Button color="secondary" variant="contained" onClick={handleClickOpen}>
          Add INTERVAL SCHEDULE
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
            backgroundColor: colors.greenAccent[600],
            borderBottom: "none",
          },
          "& .MuiDataGrid-virtualScroller": {
            backgroundColor: colors.primary[400],
          },
          "& .MuiDataGrid-footerContainer": {
            borderTop: "none",
            backgroundColor: colors.greenAccent[600],
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
          onRowSelectionModelChange={handleSelectionChange}
          rowSelectionModel={selectedRows}
        />
      </Box>
    </Box>
  );
};

export default IntervalSchedule;
