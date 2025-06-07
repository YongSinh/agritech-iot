import { Box, useTheme, Button, Stack, IconButton } from "@mui/material";
import { Header } from "../../components";
import DeleteIcon from '@mui/icons-material/Delete';
import { EditRounded } from "@mui/icons-material";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";
import Swal from "sweetalert2";

const Trigger = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [triggers, setTriggers] = useState([]);
  const [sensors, setSensors] = useState([]);
  const [deviceIds, setDeviceIds] = useState([]);
  const [loading, setLoading] = useState(true);
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
    setInitialData(null)
  };

  const handleUpdate = (value) => {
    setInitialData(value);
    setOpen(true);
    setEdit(true)
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
      await request(`/iot/v1/trigger/${value.id}`, "DELETE", null);
      await getListTriggers(); // Assuming this is async
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

  const handleSubmit = async (formData) => {
    // You can now send the formData to your API or perform other actions
    let url = edit ? "/iot/v1/triggers/update" : "/iot/v1/triggers/multiple-create";
    let method = "post";
    console.log(formData);

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
      getListTriggers()
      handleClose(); // Close the dialog after submission
    } else {
      console.log(result.code)
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
    getListTriggers();
    getListSensors();
    getAllDeviceIds();
  }, []);


  const getListTriggers = async () => {
    const result = await request("/iot/v1/triggers", "GET", null);
    if (result) {
      setTriggers(result.data)
      setLoading(false)
    }
  };

  const getListSensors = async () => {
    const result = await request("/iot/v1/triggers/sensors", "GET", null);
    if (result) {
      setSensors(result.data)
      setLoading(false)
    }
  };

  const getAllDeviceIds = async () => {
    const result = await request("/iot/v1/devices", "GET", null);
    if (result) {
      setDeviceIds(result.data)
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
      field: "sensor",
      headerName: "Sensor",
      flex: 1,
    },
    {
      field: "operator",
      headerName: "Operator",
      flex: 1,
      cellClassName: "name-column--cell",
    },
    {
      field: "value",
      headerName: "Value",
      flex: 1,
    },
    {
      field: "action",
      headerName: "Action",
      flex: 1,
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
      <Header title="TRIGGER" subtitle="List of Trigger" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add Trigger
      </Button>
      <ModelForm
        open={open}
        handleClose={handleClose}
        handleSubmit={handleSubmit} // Pass the submit handler
        colors={colors}
        deviceIds={deviceIds}
        initialData={initialData}
        sensors={sensors}
        isEdit={edit}
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
          rows={triggers}
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

export default Trigger;
