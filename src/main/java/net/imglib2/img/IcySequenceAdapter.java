package net.imglib2.img;

import icy.image.IcyBufferedImage;
import icy.sequence.Sequence;

import java.util.Arrays;

import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

public class IcySequenceAdapter
{

	private static final long[] getDims( final Sequence sequence )
	{
		final int sizeX = sequence.getSizeX();
		final int sizeY = sequence.getSizeY();
		final int sizeC = sequence.getSizeC();
		final int sizeZ = sequence.getSizeZ();
		final int sizeT = sequence.getSizeT();
		/*
		 * We decide on the following dimension ordering.
		 */
		return new long[] { sizeX, sizeY, sizeC, sizeZ, sizeT };
	}

	private static final long[] getSqueezedDims( final Sequence sequence )
	{
		final long[] dims = squeezeSingletonDims( getDims( sequence ) );
		return dims;
	}


	private static final long[] squeezeSingletonDims( final long[] originalDims )
	{
		final long[] dims = new long[ originalDims.length ];
		int index = 0;
		for ( final long l : originalDims )
		{
			if ( l <= 1 )
			{
				continue;
			}
			dims[ index++ ] = l;
		}
		return Arrays.copyOf( dims, index );
	}

	/**
	 * Returns the spatial and temporal calibration of the specified sequence in
	 * a <code>double</code> array.
	 * <ol start ="0">
	 * <li>dx in um.
	 * <li>dy in um.
	 * <li>dz in um.
	 * <li>dt in s.
	 * </ol>
	 * 
	 * @param sequence
	 * @return a new, 4-elements, <code>double</code> array.
	 */
	public static final double[] getCalibration( final Sequence sequence )
	{
		final double[] calibration = new double[ 4 ]; // XYZT

		calibration[ 0 ] = sequence.getPixelSizeX();
		calibration[ 1 ] = sequence.getPixelSizeY();
		calibration[ 2 ] = sequence.getPixelSizeZ();
		calibration[ 3 ] = sequence.getTimeInterval();
		return calibration;

	}

	public static final PlanarImg< ?, ? > wrap( final Sequence sequence )
	{
		switch ( sequence.getDataType_() )
		{
		case BYTE:
			return wrapByte( sequence );
		case INT:
			return wrapInt( sequence );
		case SHORT:
			return wrapShort( sequence );
		case UBYTE:
			return wrapUnsignedByte( sequence );
		case UINT:
			return wrapUnsignedInt( sequence );
		case USHORT:
			return wrapUnsignedShort( sequence );
		case DOUBLE:
			return wrapDouble( sequence );
		case FLOAT:
			return wrapFloat( sequence );
		case LONG:
		case ULONG:
		default:
			throw new RuntimeException( "Only 8, 16, float or double supported!" );
		}
	}

	public static PlanarImg< ByteType, ByteArray > wrapByte( final Sequence sequence )
	{
		final PlanarImg< ByteType, ByteArray > img = PlanarImgs.bytes( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final byte[] data = image.getDataXYAsByte( c );
				final ByteArray plane = new ByteArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static PlanarImg< DoubleType, DoubleArray > wrapDouble( final Sequence sequence )
	{
		final PlanarImg< DoubleType, DoubleArray > img = PlanarImgs.doubles( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final double[] data = image.getDataXYAsDouble( c );
				final DoubleArray plane = new DoubleArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static final PlanarImg< FloatType, FloatArray > wrapFloat( final Sequence sequence )
	{
		final PlanarImg< FloatType, FloatArray > img = PlanarImgs.floats( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final float[] data = image.getDataXYAsFloat( c );
				final FloatArray plane = new FloatArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static PlanarImg< IntType, IntArray > wrapInt( final Sequence sequence )
	{
		final PlanarImg< IntType, IntArray > img = PlanarImgs.ints( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final int[] data = image.getDataXYAsInt( c );
				final IntArray plane = new IntArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static PlanarImg< ShortType, ShortArray > wrapShort( final Sequence sequence )
	{
		final PlanarImg< ShortType, ShortArray > img = PlanarImgs.shorts( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final short[] data = image.getDataXYAsShort( c );
				final ShortArray plane = new ShortArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static final PlanarImg< UnsignedByteType, ByteArray > wrapUnsignedByte( final Sequence sequence )
	{
		final PlanarImg< UnsignedByteType, ByteArray > img = PlanarImgs.unsignedBytes( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final byte[] data = image.getDataXYAsByte( c );
				final ByteArray plane = new ByteArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static PlanarImg< UnsignedIntType, IntArray > wrapUnsignedInt( final Sequence sequence )
	{
		final PlanarImg< UnsignedIntType, IntArray > img = PlanarImgs.unsignedInts( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final int[] data = image.getDataXYAsInt( c );
				final IntArray plane = new IntArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	public static PlanarImg< UnsignedShortType, ShortArray > wrapUnsignedShort( final Sequence sequence )
	{
		final PlanarImg< UnsignedShortType, ShortArray > img = PlanarImgs.unsignedShorts( getSqueezedDims( sequence ) );
		int no = 0;
		for ( final IcyBufferedImage image : sequence.getAllImage() )
		{
			for ( int c = 0; c < sequence.getSizeC(); c++ )
			{
				final short[] data = image.getDataXYAsShort( c );
				final ShortArray plane = new ShortArray( data );
				img.setPlane( no++, plane );
			}
		}
		return img;
	}

	private IcySequenceAdapter()
	{}
}
