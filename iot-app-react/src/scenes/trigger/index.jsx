import { Box, Button, useTheme } from "@mui/material";
import { Header } from "../../components";
import { DataGrid } from "@mui/x-data-grid";
import { tokens } from "../../theme";
import { useState, useEffect } from "react";
import { useRequest } from "../../config/api/request"
import ModelForm from "./modelForm";

const Trigger = () => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const { request } = useRequest();
  const [triggers, setTriggers] = useState([]);
  const [deviceIds, setDeviceIds] = useState([]);
  const [loading, setLoading] = useState(true);
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
    getListTriggers()
    getAllDeviceIds()
  }, []);

  const getListTriggers = async () => {
    const result = await request("/iot/api/v1/triggers", "GET", null);
    if (result) {
      setTriggers(result.data)
      setLoading(false)
      console.log("Data fetched:", result);
    }
  };

  const getAllDeviceIds = async () => {
    const result = await request("/iot/api/v1/device-ids", "GET", null);
    if (result) {
      setDeviceIds(result.data)
      setLoading(false)
      console.log("Data fetched:", result);
    }
  };

  const columns = [
    { field: "id", headerName: "ID" },
    {
      field: "operator",
      headerName: "Operator",
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
      field: "sensor",
      headerName: "Sensor",
      flex: 1,
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
