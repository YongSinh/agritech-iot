import { Box, Button } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request";
import ModelForm from "./modelForm";
import { useTheme } from "@emotion/react";
import Swal from "sweetalert2";

const Device = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [devices, setDevices] = useState([]);
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
    setInitialData(null)
  };

  const handleUpdate = (value) => {
    setInitialData(value);
    setOpen(true);
    setEdit(true)
  };

  // Fetch devices from the API
  useEffect(() => {
    getListDevice();
  }, []);

  const getListDevice = async () => {
    const result = await request("/iot/v1/devices", "GET", null);
    if (result) {
      setDevices(result.data);
      setLoading(false);
    }
  };

  // Add unique IDs to devices for DataGrid
  const devicesWithIds = devices.map((row, index) => ({
    id: row.deviceId, // Generate a unique ID
    ...row,
  }));

  // Handle form submission
  const handleSubmit = async (formData) => {
    // You can now send the formData to your API or perform other actions
    let url = edit ? "/iot/v1/update-device" : "/iot/v1/create-device";
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
      getListDevice()
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

  const columns = [
    { field: "id", headerName: "Device ID" },
    { field: "name", headerName: "Name", flex: 1, cellClassName: "name-column--cell" },
    { field: "controller", headerName: "Controller", flex: 1 },
    { field: "sensors", headerName: "Sensors", flex: 1 },
    { field: "remark", headerName: "Remark", flex: 1 },
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
    },
  ];

  return (
    <Box m="20px">
      <Header title="DEVICE" subtitle="Managing the IoT device" />
      <Button color="secondary" variant="contained" onClick={handleClickOpen}>
        Add Device
      </Button>
      <ModelForm
        open={open}
        handleClose={handleClose}
        handleSubmit={handleSubmit} // Pass the submit handler
        colors={colors}
        initialData={initialData}
      />
      <Box
        mt="40px"
        height="75vh"
        flex={1}
        sx={{
          "& .MuiDataGrid-root": { border: "none" },
          "& .MuiDataGrid-cell": { border: "none" },
          "& .name-column--cell": { color: colors.greenAccent[300] },
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
      > {import.meta.env.VITE_KEYCLOAK_URL}
        <DataGrid
          rows={devicesWithIds}
          columns={columns}
          loading={loading}
          initialState={{
            pagination: {
              paginationModel: { pageSize: 10 },
            },
          }}
          checkboxSelection
        />
      </Box>
    </Box>
  );
};

export default Device;