package org.embulk.input.jdbc.getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.embulk.spi.Column;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampFormatter;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;

public class TimeColumnGetter
        extends AbstractColumnGetter
{
    static final String DEFAULT_FORMAT = "%H:%M:%S";
    private final TimestampFormatter timestampFormatter;
    private Time value;

    public TimeColumnGetter(PageBuilder to, Type toType, TimestampFormatter timestampFormatter)
    {
        super(to, toType);

        this.timestampFormatter = timestampFormatter;
    }

    @Override
    protected void fetch(ResultSet from, int fromIndex) throws SQLException
    {
        value = from.getTime(fromIndex);
    }

    @Override
    protected Type getDefaultToType()
    {
        return Types.TIMESTAMP.withFormat(DEFAULT_FORMAT);
    }

    @Override
    public void stringColumn(Column column)
    {
        Timestamp t = Timestamp.ofEpochMilli(value.getTime());
        to.setString(column, timestampFormatter.format(t));
    }

    @Override
    public void timestampColumn(Column column)
    {
        Timestamp t = Timestamp.ofEpochMilli(value.getTime());
        to.setTimestamp(column, t);
    }
}