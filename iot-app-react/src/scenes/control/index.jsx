import { Box, useTheme, Button, Stack, IconButton } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import DeleteIcon from '@mui/icons-material/Delete';
import { EditRounded } from "@mui/icons-material";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import dayjs from "dayjs";
import Swal from "sweetalert2";
import ModelForm from "./modelForm";

const IntervalSchedule = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [data, setData] = useState([]);
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
    let url = edit ? "/iot/v1/control-log/update" : "/iot/v1/control-log/create";
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
      getListControlLog()
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
      await request(`/iot/v1/control-logs/${value.id}`, "DELETE", null);
      await getListControlLog(); // Assuming this is async
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


const sendTaskToDevice = async (task) => {
  // Confirmation dialog before proceeding
  const confirmation = await Swal.fire({
    title: "Are you sure?",
    text: "You won't be able to revert this!",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",  // Blue confirm button
    cancelButtonColor: "#d33",      // Red cancel button
    confirmButtonText: "Yes, send it!",
    cancelButtonText: "Cancel",
    reverseButtons: true,           // Places "Confirm" on the right
  });

  // Exit if user cancels
  if (!confirmation.isConfirmed) return;

  try {
    // Send the task to the device
    await request(`/iot/v1/control-logs/send-task/${task.id}`, "POST", null);
    
    // Refresh the task list
    await getListControlLog(); 

    // Success notification
    await Swal.fire({
      title: "Task Sent!",
      text: "Your task has been successfully sent.",
      icon: "success",
      timer: 2000,  // Auto-close after 2 seconds
      showConfirmButton: false,
    });
  } catch (error) {
    console.error("Failed to send task:", error);

    // Error notification
    await Swal.fire({
      title: "Error!",
      text: "Failed to send the task. Please try again.",
      icon: "error",
    });
  }
};

  useEffect(() => {
    getListControlLog()
    getAllDeviceIds()
  }, []);


  const getAllDeviceIds = async () => {
    const result = await request("/iot/v1/device/ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
    }
  };


  const getListControlLog = async () => {
    const result = await request("/iot/v1/control-logs", "GET", null);
    if (result) {
      setData(result.data)
      setLoading(false)
    }
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
      cellClassName: "name-column--cell",
    },
    {
      field: "status",
      headerName: "Status",
      flex: 1,
    },
    {
      field: "sentBy",
      headerName: "Sent By",
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
      headerName: "Send",
      field: "Send",
      flex: 0.5,
      headerAlign: "center",
      align: "center",
      renderCell: ({ row }) => {
        return (
          <Button color="secondary" variant="contained" onClick={() => sendTaskToDevice(row)}>
            send
          </Button>
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
      <Header title="CONTROL LOGS" subtitle="Managing the IOT Deivce" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add CONTROL LOGS
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
          rows={data}
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
